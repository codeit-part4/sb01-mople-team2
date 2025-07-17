package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentCardResponse;
import com.sprint.mople.domain.content.entity.ContentSortType;
import com.sprint.mople.domain.content.repository.ContentLikeRepository;
import com.sprint.mople.domain.content.repository.ContentRecommendRepository;
import com.sprint.mople.domain.content.repository.ContentRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentRecommendService {

  private final ContentRecommendRepository recommendRepository;
  private final ContentRepository contentRepository;
  private final ContentLikeRepository contentLikeRepository;

  public List<ContentCardResponse> getRecommendedContents(UUID userId, int limit) {
    List<Object[]> rawList = recommendRepository.findTopRecommended(limit);

    return getContentResponses(userId, rawList);
  }

  public List<ContentCardResponse> getSortedContents(UUID userId, int limit, ContentSortType sortType) {
    List<Object[]> rawList = switch (sortType) {
      case RECENT -> recommendRepository.findAllByRecent(limit);
      case MOST_REVIEWED -> recommendRepository.findAllByReviewCount(limit);
      case SCORE -> recommendRepository.findAllByScore(limit);
    };

    return getContentResponses(userId, rawList);
  }

  private List<ContentCardResponse> getContentResponses(UUID userId, List<Object[]> rawList) {
    return rawList
        .stream()
        .map(row -> {
          UUID contentId = (UUID) row[0];
          String title = (String) row[1];
          Instant createdAt = (Instant) row[2];
          Instant updatedAt = (Instant) row[3];
          long reviewCount = ((Number) row[4]).longValue();
          BigDecimal averageRating = (BigDecimal) row[5];
          String posterUrl = (String) row[6];
          long likeCount = ((Number) row[7]).longValue();
          double score = ((Number) row[8]).doubleValue();
          boolean liked = contentLikeRepository.existsByUserIdAndContentId(userId, contentId);

          return ContentCardResponse
              .builder()
              .id(contentId)
              .title(title)
              .likeCount(likeCount)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .image(posterUrl)
              .viewers(reviewCount)
              //TODO:janghoosa 임시 시청자수
              .reviews(reviewCount)
              .rating(averageRating)
              .liked(liked)
              .build();
        })
        .toList();
  }
}

