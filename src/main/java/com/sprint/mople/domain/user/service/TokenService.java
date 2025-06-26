package com.sprint.mople.domain.user.service;

import java.util.Map;
import java.util.UUID;

public interface TokenService {
  Map<String, String> generateTokens(UUID userId, String email);
  String refreshAccessToken(String refreshToken);

}
