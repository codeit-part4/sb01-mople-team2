package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentResponse;
import com.sprint.mople.domain.content.entity.ContentSortType;
import com.sprint.mople.domain.content.repository.ContentLikeRepository;
import com.sprint.mople.domain.content.repository.ContentRecommendRepository;
import com.sprint.mople.domain.content.repository.ContentRepository;
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

  public List<ContentResponse> getRecommendedContents(UUID userId, int limit) {
    List<Object[]> rawList = recommendRepository.findTopRecommended(limit);

    return rawList
        .stream()
        .map(row -> {
          UUID contentId = (UUID) row[0];
          String title = (String) row[1];
          Instant createdAt = (Instant) row[2];
          Instant updatedAt = (Instant) row[3];
          long likeCount = ((Number) row[7]).longValue();

          boolean liked = contentLikeRepository.existsByUserIdAndContentId(userId, contentId);

          return new ContentResponse(contentId, title, likeCount, createdAt, updatedAt, liked);
        })
        .toList();
  }

  public List<ContentResponse> getSortedContents(UUID userId, int limit, ContentSortType sortType) {
    List<Object[]> rawList = switch (sortType) {
      case RECENT -> recommendRepository.findAllByRecent(limit);
      case MOST_REVIEWED -> recommendRepository.findAllByReviewCount(limit);
      case SCORE -> recommendRepository.findAllByScore(limit);
    };

    return rawList.stream().map(row -> {
      UUID id = (UUID) row[0];
      String title = (String) row[1];
      Instant createdAt = (Instant) row[2];
      Instant updatedAt = (Instant) row[3];
      long likeCount = ((Number) row[4]).longValue();
      boolean liked = contentLikeRepository.existsByUserIdAndContentId(userId, id);
      return new ContentResponse(id, title, likeCount, createdAt, updatedAt, liked);
    }).toList() ;
  }
}

