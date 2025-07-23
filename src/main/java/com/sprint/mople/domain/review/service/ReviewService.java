package com.sprint.mople.domain.review.service;

import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

  ReviewResponse createReview(UUID contentId, ReviewCreateRequest request);

  List<ReviewResponse> getAllReviews(Pageable pageable);

  List<ReviewResponse> getReviewsByUserId(UUID userId);

  List<ReviewResponse> getReviewsByContentId(UUID contentId, Pageable pageable);

  ReviewResponse getReviewById(UUID id);

  ReviewResponse updateReviewById(
      UUID id,
      ReviewUpdateRequest request
  );

  void deleteReview(UUID id);
}
