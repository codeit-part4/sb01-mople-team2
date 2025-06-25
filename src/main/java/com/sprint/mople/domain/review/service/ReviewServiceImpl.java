package com.sprint.mople.domain.review.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import com.sprint.mople.domain.review.entity.Review;
import com.sprint.mople.domain.review.exception.ReviewNotFoundException;
import com.sprint.mople.domain.review.repository.ReviewRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final ContentRepository contentRepository;

  @Override
  public ReviewResponse createReview(ReviewCreateRequest request) {
    User user = userRepository
        .findById(request.userId())
        .orElseThrow();
    Content content = contentRepository
        .findById(request.contentId())
        .orElseThrow();
    Review newReview = Review
        .builder()
        .user(user)
        .content(content)
        .rating(request.rating())
        .comment(request.comment())
        .build();
    return ReviewResponse.from(reviewRepository.save(newReview));
  }

  @Override
  public List<ReviewResponse> getAllReviews() {
    return reviewRepository
        .findAll()
        .stream()
        .map(ReviewResponse::from)
        .toList();
  }

  @Override
  public List<ReviewResponse> getReviewsByUserId(UUID userId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow();
    return reviewRepository
        .findByUser(user)
        .stream()
        .map(ReviewResponse::from)
        .toList();
  }

  @Override
  public ReviewResponse getReviewById(UUID id) {
    return ReviewResponse.from(reviewRepository
        .findById(id)
        .orElseThrow(ReviewNotFoundException::new));
  }

  @Override
  public ReviewResponse updateReviewById(
      UUID id,
      ReviewUpdateRequest request
  )
  {
    Review review = reviewRepository
        .findById(id)
        .orElseThrow(ReviewNotFoundException::new);
    if (request.comment() != null) {
      review.updateComment(request.comment());
    }
    if (request.rating() != null) {
      review.updateRating(request.rating());
    }
    Review updatedReview = reviewRepository.save(review);
    return ReviewResponse.from(updatedReview);
  }

  @Override
  public void deleteReview(UUID id) {
    reviewRepository
        .findById(id)
        .orElseThrow(ReviewNotFoundException::new);
    reviewRepository.deleteById(id);
  }

}
