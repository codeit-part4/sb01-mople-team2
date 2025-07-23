package com.sprint.mople.domain.follow.dto;

import java.util.UUID;

public record FollowResponse(
    UUID followerId,
    UUID followeeId
) {

}
