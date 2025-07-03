package com.sprint.mople.domain.review.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.review.dto.ContentReviewStats;
import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import com.sprint.mople.domain.review.entity.Review;
import com.sprint.mople.domain.review.exception.ReviewNotFoundException;
import com.sprint.mople.domain.review.repository.ReviewRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock private ReviewRepository reviewRepository;

  @Mock private UserRepository userRepository;

  @Mock private ContentRepository contentRepository;

  @InjectMocks private ReviewServiceImpl reviewService;

  private User user;
  private Content content;
  private Review review;
  private UUID userId;
  private UUID contentId;
  private UUID reviewId;

  @BeforeEach
  void 설정() {
    userId = UUID.randomUUID();
    contentId = UUID.randomUUID();
    reviewId = UUID.randomUUID();

    user = User
        .builder()
        .id(userId)
        .userName("testuser")
        .build();

    content = Content
        .builder()
        .title("test content")
        .build();
    ReflectionTestUtils.setField(content, "id", contentId);

    review = Review
        .builder()
        .user(user)
        .content(content)
        .rating(4)
        .comment("good")
        .build();
    ReflectionTestUtils.setField(review, "id", reviewId);
  }

  @Test
  void 리뷰_생성_성공() {
    ReviewCreateRequest request = new ReviewCreateRequest(userId, "Excellent!", 5);
    ContentReviewStats stats = new ContentReviewStats(3.5, 5L);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
      Review arg = invocation.getArgument(0);
      ReflectionTestUtils.setField(arg, "id", reviewId);
      return arg;
    });
    when(reviewRepository.findReviewStatsByContentId(contentId)).thenReturn(stats);

    ReviewResponse response = reviewService.createReview(contentId, request);

    assertThat(response).isNotNull();
    assertThat(response.rating()).isEqualTo(5);
    assertThat(response.comment()).isEqualTo("Excellent!");
    assertThat(response.user()).isEqualTo(userId);
    assertThat(response.content()).isEqualTo(contentId);

    verify(userRepository).findById(userId);
    verify(contentRepository, times(2)).findById(contentId);
    verify(reviewRepository).save(any(Review.class));
  }

  @Test
  void 전체_리뷰_조회_리스트_반환() {
    Pageable pageable = PageRequest.of(
        0, 20, Sort
            .by("createdAt")
            .descending()
    );

    when(reviewRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(review)));

    List<ReviewResponse> responses = reviewService.getAllReviews(pageable);

    assertThat(responses).hasSize(1);
    assertThat(responses
        .get(0)
        .id()).isEqualTo(reviewId);

    verify(reviewRepository).findAll(pageable);
  }

  @Test
  void 사용자ID로_리뷰_조회_성공() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(reviewRepository.findByUser(user)).thenReturn(List.of(review));

    List<ReviewResponse> responses = reviewService.getReviewsByUserId(userId);

    assertThat(responses).hasSize(1);
    assertThat(responses
        .get(0)
        .user()).isEqualTo(userId);

    verify(userRepository).findById(userId);
    verify(reviewRepository).findByUser(user);
  }

  @Test
  void 리뷰ID로_조회_성공() {
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

    ReviewResponse response = reviewService.getReviewById(reviewId);

    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(reviewId);

    verify(reviewRepository).findById(reviewId);
  }

  @Test
  void 리뷰ID_조회_없음_예외발생() {
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    assertThrows(ReviewNotFoundException.class, () -> reviewService.getReviewById(reviewId));
    verify(reviewRepository).findById(reviewId);
  }

  @Test
  void 리뷰ID로_리뷰_수정_성공() {
    ReviewUpdateRequest updateRequest = new ReviewUpdateRequest("Updated comment", 3);
    ContentReviewStats stats = new ContentReviewStats(3.5, 5L);

    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));
    when(reviewRepository.findReviewStatsByContentId(contentId)).thenReturn(stats);

    ReviewResponse updated = reviewService.updateReviewById(reviewId, updateRequest);

    assertThat(updated.comment()).isEqualTo("Updated comment");
    assertThat(updated.rating()).isEqualTo(3);

    verify(reviewRepository).findById(reviewId);
    verify(reviewRepository).save(any(Review.class));
  }

  @Test
  void 리뷰_삭제_성공() {
    ContentReviewStats stats = new ContentReviewStats(3.5, 5L);

    when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(reviewRepository.findReviewStatsByContentId(contentId)).thenReturn(stats);

    reviewService.deleteReview(reviewId);

    verify(reviewRepository).findById(reviewId);
    verify(reviewRepository).deleteById(reviewId);
  }

  @Test
  void 리뷰_삭제_없음_예외발생() {
    when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

    assertThrows(ReviewNotFoundException.class, () -> reviewService.deleteReview(reviewId));

    verify(reviewRepository).findById(reviewId);
    verify(reviewRepository, never()).deleteById(any());
  }
}
