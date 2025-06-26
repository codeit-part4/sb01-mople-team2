package com.sprint.mople.domain.review.service;

import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ReviewService {

  ReviewResponse createReview(ReviewCreateRequest request);

  List<ReviewResponse> getAllReviews();

  List<ReviewResponse> getReviewsByUserId(UUID userId);

  ReviewResponse getReviewById(UUID id);

  ReviewResponse updateReviewById(
      UUID id,
      ReviewUpdateRequest request
  );

  void deleteReview(UUID id);
}
