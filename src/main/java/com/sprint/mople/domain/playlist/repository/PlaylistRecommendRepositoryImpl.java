package com.sprint.mople.domain.playlist.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistResponse;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistSortType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.sprint.mople.domain.content.entity.QContent.content;
import static com.sprint.mople.domain.playlist.entity.QPlaylist.playlist;
import static com.sprint.mople.domain.playlist.entity.QPlaylistCategory.playlistCategory;
import static com.sprint.mople.domain.playlist.entity.QPlaylistCategoryMapping.playlistCategoryMapping;
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
      int pageSize,
      String query,
      PlaylistSortType searchType
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

    NumberExpression<Double> categoryScore = new CaseBuilder()
        .when(playlistCategory.name.in(userCategories))
        .then(MAX_CATEGORY_SCORE)
        .otherwise(0.0);

    NumberExpression<Double> totalScore = subscriptionScore
        .add(likeScore)
        .add(avgRatingScore)
        .add(categoryScore)
        .coalesce(0.0);

    BooleanExpression baseCondition = playlist.isPublic.isTrue();

    if (query != null && !query.isBlank()) {
      BooleanExpression queryCondition = playlist.title
          .containsIgnoreCase(query)
          .or(user.userName.containsIgnoreCase(query));
      baseCondition = baseCondition.and(queryCondition);
    }

    BooleanExpression cursorCondition = null;
    if (lastScore != null && lastId != null) {
      cursorCondition = totalScore
          .lt(lastScore)
          .or(totalScore
              .eq(lastScore)
              .and(playlist.id.gt(lastId)));
    }

    OrderSpecifier<?> orderSpecifier = totalScore.desc();

// searchType에 따라 정렬 변경
    if (searchType != null) {
      switch (searchType) {
        case RECENT -> orderSpecifier = playlist.updatedAt.desc();
        case MOST_SUBSCRIBED -> orderSpecifier = subscription
            .countDistinct()
            .desc();
        case MOST_LIKED -> orderSpecifier = playlistLike
            .countDistinct()
            .desc();
        case HIGHEST_RATED -> orderSpecifier = content.averageRating
            .avg()
            .desc();
        // 기본값은 추천 점수 기반
      }
    }

    List<Tuple> raw = queryFactory
        .select(playlist, totalScore)
        .from(playlist)
        .leftJoin(playlist.subscriptions, subscription)
        .leftJoin(playlist.playlistLikes, playlistLike)
        .leftJoin(playlist.playlistContents, playlistContent)
        .leftJoin(playlistContent.content, content)
        .leftJoin(playlist.user, user)
        .leftJoin(playlist.categories, playlistCategoryMapping)
        .leftJoin(playlistCategoryMapping.category, playlistCategory)
        .where(baseCondition)
        .groupBy(playlist.id, user.id, playlistCategory.name)
        .having(cursorCondition)
        .orderBy(orderSpecifier, playlist.id.asc())
        .limit(pageSize)
        .fetch();

    return raw
        .stream()
        .map(tuple -> {
          Playlist p = tuple.get(playlist);
          double score = tuple.get(totalScore) != null ? tuple.get(totalScore) : 0.0;

          return RecommendedPlaylistResponse.from(p, score);
        })
        .toList();
  }
}

