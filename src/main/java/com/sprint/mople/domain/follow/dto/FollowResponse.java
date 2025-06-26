package com.sprint.mople.domain.follow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record FollowResponse(
    UUID followerId,
    UUID followeeId
) {

}
