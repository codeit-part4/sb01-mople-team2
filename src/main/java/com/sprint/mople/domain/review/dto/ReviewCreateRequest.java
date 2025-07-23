package com.sprint.mople.domain.review.dto;

import java.util.UUID;

public record ReviewCreateRequest(
    UUID userId,
    String comment,
    int rating
)
{

}
