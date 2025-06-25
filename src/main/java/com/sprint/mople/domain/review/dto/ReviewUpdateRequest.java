package com.sprint.mople.domain.review.dto;

public record ReviewUpdateRequest(
    String comment,
    Integer rating
) {

}
