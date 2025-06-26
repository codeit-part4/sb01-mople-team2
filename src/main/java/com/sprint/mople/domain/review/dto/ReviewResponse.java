package com.sprint.mople.domain.review.dto;


import com.sprint.mople.domain.review.entity.Review;
import java.util.UUID;

public record ReviewResponse(
    UUID id,
    // TODO : Content, User DTO로 변경 필요
    UUID content,
    UUID user,
    String comment,
    int rating,
    String createdAt
)
{

  public static ReviewResponse from(Review review) {
    return new ReviewResponse(
        review.getId(),
        review.getContent().getId(),
        review.getUser().getId(),
        review.getComment(),
        review.getRating(),
        review.getCreatedAt().toString()
    );
  }
}
