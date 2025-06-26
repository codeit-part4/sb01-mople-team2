package com.sprint.mople.domain.user.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserLoginResponse {
  private String accessToken;
  private String tokenType; // "Bearer"
  private long expiresIn;   // 단위: 초
  private UUID userId;
  private String email;
  private String name;
}

