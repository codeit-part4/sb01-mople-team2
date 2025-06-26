package com.sprint.mople.domain.review.controller;

import com.sprint.mople.domain.review.dto.ReviewCreateRequest;
import com.sprint.mople.domain.review.dto.ReviewResponse;
import com.sprint.mople.domain.review.dto.ReviewUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Review", description = "리뷰 관련 API")
@RequestMapping("/api/reviews")
public interface ReviewApi {

  @Operation(summary = "리뷰 생성", description = "새로운 리뷰를 작성합니다.")
  @PostMapping
  ResponseEntity<ReviewResponse> createReview(
      @Valid @RequestBody ReviewCreateRequest request
  );

  @Operation(summary = "전체 리뷰 조회", description = "모든 리뷰를 조회합니다.")
  @GetMapping
  ResponseEntity<List<ReviewResponse>> getAllReviews();

  @Operation(summary = "사용자 리뷰 조회", description = "특정 사용자의 리뷰들을 조회합니다.")
  @GetMapping("/user/{userId}")
  ResponseEntity<List<ReviewResponse>> getReviewsByUserId(
      @Parameter(description = "사용자 ID", required = true)
      @PathVariable UUID userId
  );

  @Operation(summary = "리뷰 단건 조회", description = "ID로 리뷰를 조회합니다.")
  @GetMapping("/{id}")
  ResponseEntity<ReviewResponse> getReviewById(
      @Parameter(description = "리뷰 ID", required = true)
      @PathVariable UUID id
  );

  @Operation(summary = "리뷰 수정", description = "기존 리뷰를 수정합니다.")
  @PutMapping("/{id}")
  ResponseEntity<ReviewResponse> updateReview(
      @Parameter(description = "리뷰 ID", required = true)
      @PathVariable UUID id,
      @Valid @RequestBody ReviewUpdateRequest request
  );

  @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteReview(
      @Parameter(description = "리뷰 ID", required = true)
      @PathVariable UUID id
  );
}
