package com.sprint.mople.domain.playlist.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistResponse;
import com.sprint.mople.domain.playlist.entity.Playlist;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.sprint.mople.domain.content.entity.QContent.content;
import static com.sprint.mople.domain.playlist.entity.QPlaylist.playlist;
import static com.sprint.mople.domain.playlist.entity.QPlaylistContent.playlistContent;
import static com.sprint.mople.domain.playlist.entity.QPlaylistLike.playlistLike;
import static com.sprint.mople.domain.playlist.entity.QSubscription.subscription;
import static com.sprint.mople.domain.user.entity.QUser.user;

@Slf4j
@RequiredArgsConstructor
public class PlaylistRecommendRepositoryImpl implements PlaylistRecommendRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<RecommendedPlaylistResponse> findRecommendedByUserCategoriesWithCursor(
      List<String> userCategories,
      Double lastScore,
      UUID lastId,
      int pageSize
  )
  {
    double MAX_SUBSCRIPTION_SCORE = 0.3;
    double MAX_LIKE_SCORE = 0.25;
    double MAX_AVG_RATING_SCORE = 0.25;
    double MAX_CATEGORY_SCORE = 0.2;

    NumberExpression<Double> subscriptionScore = new CaseBuilder()
        .when(subscription
            .countDistinct()
            .goe(100L))
        .then(MAX_SUBSCRIPTION_SCORE)
        .otherwise(subscription
            .countDistinct()
            .castToNum(Double.class)
            .divide(100.0)
            .multiply(MAX_SUBSCRIPTION_SCORE));

    NumberExpression<Double> likeScore = new CaseBuilder()
        .when(playlistLike
            .countDistinct()
            .goe(100L))
        .then(MAX_LIKE_SCORE)
        .otherwise(playlistLike
            .countDistinct()
            .castToNum(Double.class)
            .divide(100.0)
            .multiply(MAX_LIKE_SCORE));

    NumberExpression<Double> avgRatingScore = content.averageRating
        .avg()
        .coalesce(0.0)
        .divide(5.0)
        .multiply(MAX_AVG_RATING_SCORE);
//    NumberExpression<Double> categoryScore = category.name.in(userCategories).countDistinct().multiply(0.2);

    NumberExpression<Double> totalScore = subscriptionScore
        .add(likeScore)
        .add(avgRatingScore)
        .coalesce(0.0);
//        .add(categoryScore);

    BooleanExpression baseCondition = playlist.isPublic.isTrue();

    BooleanExpression cursorCondition = null;
    if (lastScore != null && lastId != null) {
      cursorCondition = totalScore
          .lt(lastScore)
          .or(totalScore
              .eq(lastScore)
              .and(playlist.id.gt(lastId)));
    }
    NumberExpression<Long> subCount = subscription.countDistinct();
    NumberExpression<Long> likeCount = playlistLike.countDistinct();
    NumberExpression<Double> avgRating = content.averageRating.avg().coalesce(0.0);


    List<Tuple> raw = queryFactory
        .select(
            playlist,
            subCount,
            likeCount,
            avgRating
        )
        .from(playlist)
        .leftJoin(playlist.subscriptions, subscription)
        .leftJoin(playlist.playlistLikes, playlistLike)
        .leftJoin(playlist.playlistContents, playlistContent)
        .leftJoin(playlistContent.content, content)
        .leftJoin(playlist.user, user)
        .fetchJoin()
        .where(baseCondition)
        .groupBy(playlist.id, user.id)
        .having(cursorCondition)
        .orderBy(totalScore.desc(), playlist.id.asc())
        .limit(pageSize)
        .fetch();

    return raw
        .stream()
        .map(tuple -> {
          Playlist p = tuple.get(playlist);
          long sub = tuple.get(subCount) != null ? tuple.get(subCount) : 0;
          long like = tuple.get(likeCount) != null ? tuple.get(likeCount) : 0;
          double rating = tuple.get(avgRating) != null ? tuple.get(avgRating) : 0.0;
          log.info(
              "Recommending playlist: {}, subscriptionCount: {}, likeCount: {} ", Objects
                  .requireNonNull(p)
                  .getId(), sub, like
          );
          double score =
              Math.min(1.0, sub / 100.0) * MAX_SUBSCRIPTION_SCORE +
              Math.min(1.0, like / 100.0) * MAX_LIKE_SCORE +
              (rating / 5.0) * MAX_AVG_RATING_SCORE;

          return RecommendedPlaylistResponse.from(p, score);
        })
        .toList();
  }
}

