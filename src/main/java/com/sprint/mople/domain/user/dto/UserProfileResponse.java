package com.sprint.mople.domain.user.dto;

import java.util.UUID;

public record UserProfileResponse(
    UUID userid,
    String email,
    String name
) {

}
