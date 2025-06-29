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
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final ContentRepository contentRepository;

  @Override
  public ReviewResponse createReview(UUID contentId, ReviewCreateRequest request) {
    log.info("Creating review for contentId={} by userId={}", contentId, request.userId());

    User user = userRepository.findById(request.userId()).orElseThrow();
    Content content = contentRepository.findById(contentId).orElseThrow();

    Review newReview = Review.builder()
        .user(user)
        .content(content)
        .rating(request.rating())
        .comment(request.comment())
        .build();

    Review savedReview = reviewRepository.save(newReview);
    log.debug("Saved review: {}", savedReview.getId());

    updateContentReviewStats(contentId);

    return ReviewResponse.from(savedReview);
  }

  @Override
  public List<ReviewResponse> getAllReviews(Pageable pageable) {
    log.info("Fetching all reviews: pageNumber={}, pageSize={}", pageable.getPageNumber(), pageable.getPageSize());

    return reviewRepository.findAll(pageable)
        .stream()
        .map(ReviewResponse::from)
        .toList();
  }

  @Override
  public List<ReviewResponse> getReviewsByUserId(UUID userId) {
    log.info("Fetching reviews by userId={}", userId);

    User user = userRepository.findById(userId).orElseThrow();
    return reviewRepository.findByUser(user)
        .stream()
        .map(ReviewResponse::from)
        .toList();
  }

  @Override
  public List<ReviewResponse> getReviewsByContentId(UUID contentId, Pageable pageable) {
    log.info("Fetching reviews by contentId={} with pagination", contentId);

    Content content = contentRepository.findById(contentId).orElseThrow();
    return reviewRepository.findAllByContent(content, pageable)
        .stream()
        .map(ReviewResponse::from)
        .toList();
  }

  @Override
  public ReviewResponse getReviewById(UUID id) {
    log.info("Fetching review by id={}", id);

    return ReviewResponse.from(
        reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new)
    );
  }

  private void updateContentReviewStats(UUID contentId) {
    log.debug("Updating content review stats for contentId={}", contentId);

    ContentReviewStats stats = reviewRepository.findReviewStatsByContentId(contentId);
    Content content = contentRepository.findById(contentId).orElseThrow();

    log.debug("New stats - averageRating={}, count={}", stats.averageRating(), stats.reviewCount());

    content.updateAverageRating(BigDecimal.valueOf(stats.averageRating()));
    content.updateTotalRatingCount(stats.reviewCount());
  }

  @Override
  public ReviewResponse updateReviewById(UUID id, ReviewUpdateRequest request) {
    log.info("Updating review id={}", id);

    Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    if (request.comment() != null) {
      log.debug("Updating comment to: {}", request.comment());
      review.updateComment(request.comment());
    }
    if (request.rating() != null) {
      log.debug("Updating rating to: {}", request.rating());
      review.updateRating(request.rating());
    }

    Review updatedReview = reviewRepository.save(review);
    updateContentReviewStats(review.getContent().getId());

    return ReviewResponse.from(updatedReview);
  }

  @Override
  public void deleteReview(UUID id) {
    log.info("Deleting review with id={}", id);

    Review review = reviewRepository.findById(id).orElseThrow(ReviewNotFoundException::new);
    UUID contentId = review.getContent().getId();

    reviewRepository.deleteById(id);
    updateContentReviewStats(contentId);
  }
}
