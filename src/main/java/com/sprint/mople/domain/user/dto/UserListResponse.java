package com.sprint.mople.domain.user.dto;

import java.time.Instant;

public record UserListResponse(
    String userName,
    String email,
    Boolean isLocked,
    Instant createAt
) {

}

