package com.sprint.mople.domain.review.controller;

import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import com.sprint.mople.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController implements ReviewApi {

  private final ReviewService reviewService;

  @PostMapping
  public ResponseEntity<ReviewResponse> createReview(
      @Valid @RequestBody ReviewCreateRequest request
  )
  {
    ReviewResponse response = reviewService.createReview(request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<List<ReviewResponse>> getAllReviews() {
    List<ReviewResponse> reviews = reviewService.getAllReviews();
    return ResponseEntity.ok(reviews);
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(
      @PathVariable UUID userId
  )
  {
    List<ReviewResponse> reviews = reviewService.getReviewsByUserId(userId);
    return ResponseEntity.ok(reviews);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewResponse> getReviewById(
      @PathVariable UUID id
  )
  {
    ReviewResponse review = reviewService.getReviewById(id);
    return ResponseEntity.ok(review);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReviewResponse> updateReview(
      @PathVariable UUID id,
      @Valid @RequestBody ReviewUpdateRequest request
  )
  {
    ReviewResponse updated = reviewService.updateReviewById(id, request);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReview(
      @PathVariable UUID id
  )
  {
    reviewService.deleteReview(id);
    return ResponseEntity
        .noContent()
        .build();
  }
}
