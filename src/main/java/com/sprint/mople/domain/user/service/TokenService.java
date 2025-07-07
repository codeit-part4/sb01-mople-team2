package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.dto.UserLoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

public interface TokenService {

  Map<String, String> generateTokens(UUID userId, String email);

  String refreshAccessToken(String refreshToken);
  public Map<String, String> reissueTokens(String refreshToken, HttpServletResponse response);

  void logout(String refreshToken, HttpServletResponse response);
}
