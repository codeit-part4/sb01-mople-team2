package com.sprint.mople.domain.review.dto;

import java.util.UUID;

public record ReviewCreateRequest(
    UUID contentId,
    UUID userId,
    String comment,
    int rating
)
{

}
