package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.dto.ContentResponse;
import com.sprint.mople.domain.content.entity.ContentSortType;
import com.sprint.mople.domain.content.repository.ContentLikeRepository;
import com.sprint.mople.domain.content.repository.ContentRecommendRepository;
import com.sprint.mople.domain.content.repository.ContentRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentRecommendServiceTest {

  private final UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private final UUID contentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1");

  @Mock
  private ContentRecommendRepository recommendRepository;

  @Mock
  private ContentRepository contentRepository;

  @Mock
  private ContentLikeRepository contentLikeRepository;

  @InjectMocks
  private ContentRecommendService contentRecommendService;

  @Test
  @DisplayName("getRecommendedContents - 추천 콘텐츠 반환")
  void getRecommendedContents_returnsMappedList() {
    Instant now = Instant.now();
    List<Object[]> rawList = List.of(new Object[][]{
        new Object[]{
            contentId, "Test Title", now, now, 5L, BigDecimal.valueOf(4.7), "poster.jpg", 10L, 0.95
        }
    });

    when(recommendRepository.findTopRecommended(1)).thenReturn(rawList);
    when(contentLikeRepository.existsByUserIdAndContentId(userId, contentId)).thenReturn(true);

    List<ContentResponse> result = contentRecommendService.getRecommendedContents(userId, 1);

    assertThat(result).hasSize(1);
    ContentResponse content = result.get(0);
    assertThat(content.id()).isEqualTo(contentId);
    assertThat(content.title()).isEqualTo("Test Title");
    assertThat(content.averageRating()).isEqualTo(BigDecimal.valueOf(4.7));
    assertThat(content.posterUrl()).isEqualTo("poster.jpg");
    assertThat(content.totalRatingCount()).isEqualTo(5);
    assertThat(content.liked()).isTrue();
  }

  @Test
  @DisplayName("getSortedContents - 최신순 반환")
  void getSortedContents_returnsRecentSorted() {
    Instant now = Instant.now();
    List<Object[]> rawList = List.of(new Object[][]{
        new Object[]{
            contentId, "New Title", now, now, 7L, BigDecimal.valueOf(4.3), "recent.jpg", 15L, 0.85
        }
    });

    when(recommendRepository.findAllByRecent(1)).thenReturn(rawList);
    when(contentLikeRepository.existsByUserIdAndContentId(userId, contentId)).thenReturn(false);

    List<ContentResponse> result = contentRecommendService.getSortedContents(
        userId,
        1,
        ContentSortType.RECENT
    );

    assertThat(result).hasSize(1);
    ContentResponse content = result.get(0);
    assertThat(content.title()).isEqualTo("New Title");
    assertThat(content.liked()).isFalse();
  }

  @Test
  @DisplayName("getSortedContents - 리뷰 많은 순 반환")
  void getSortedContents_returnsMostReviewedSorted() {
    Instant now = Instant.now();
    List<Object[]> rawList = List.of(new Object[][]{
        new Object[]{
            contentId,
            "Reviewed Title",
            now,
            now,
            20L,
            BigDecimal.valueOf(4.0),
            "reviewed.jpg",
            30L,
            0.88
        }
    });

    when(recommendRepository.findAllByReviewCount(1)).thenReturn(rawList);
    when(contentLikeRepository.existsByUserIdAndContentId(userId, contentId)).thenReturn(false);

    List<ContentResponse> result = contentRecommendService.getSortedContents(
        userId,
        1,
        ContentSortType.MOST_REVIEWED
    );

    assertThat(result).hasSize(1);
    assertThat(result
        .get(0)
        .totalRatingCount()).isEqualTo(20);
  }

  @Test
  @DisplayName("getSortedContents - 점수 기반 반환")
  void getSortedContents_returnsScoreSorted() {
    Instant now = Instant.now();
    List<Object[]> rawList = List.of(new Object[][]{
        new Object[]{
            contentId,
            "Scored Title",
            now,
            now,
            12L,
            BigDecimal.valueOf(4.2),
            "scored.jpg",
            22L,
            0.91
        }
    });

    when(recommendRepository.findAllByScore(1)).thenReturn(rawList);
    when(contentLikeRepository.existsByUserIdAndContentId(userId, contentId)).thenReturn(true);

    List<ContentResponse> result = contentRecommendService.getSortedContents(
        userId,
        1,
        ContentSortType.SCORE
    );

    assertThat(result).hasSize(1);
    assertThat(result
        .get(0)
        .averageRating()).isNotNull();
    assertThat(result
        .get(0)
        .title()).isEqualTo("Scored Title");
    assertThat(result
        .get(0)
        .liked()).isTrue();
  }
}
