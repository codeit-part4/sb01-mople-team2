package com.sprint.mople.domain.user.dto;

import java.util.UUID;

public record UserLoginResponse(
    String accessToken,
    String tokenType, // "Bearer"
    long expiresIn,   // 단위: 초
    UUID userId,
    String email,
    String name
) {

}


