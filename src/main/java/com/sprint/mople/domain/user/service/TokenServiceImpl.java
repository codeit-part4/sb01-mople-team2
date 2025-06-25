package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.entity.RefreshToken;
import com.sprint.mople.domain.user.repository.RefreshTokenRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{
  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public TokenServiceImpl(JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository) {
    this.jwtProvider = jwtProvider;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public Map<String, String> generateTokens(UUID userId, String email) {
    String accessToken = jwtProvider.createToken(userId.toString(), email);
    String refreshToken = jwtProvider.createRefreshToken(userId.toString());

    RefreshToken tokenEntity = refreshTokenRepository.findByUserId(userId)
        .orElse(new RefreshToken());
    tokenEntity.setUserId(userId);
    tokenEntity.setToken(refreshToken);
    tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + jwtProvider.getRefreshExpirationMillis()));
    refreshTokenRepository.save(tokenEntity);

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);
    return tokens;
  }

  public String refreshAccessToken(String refreshToken) {
    Claims claims = jwtProvider.getClaims(refreshToken);
    String userIdStr = claims.getSubject();
    UUID userId = UUID.fromString(userIdStr);

    RefreshToken tokenEntity = refreshTokenRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Refresh token not found"));

    if (!tokenEntity.getToken().equals(refreshToken)) {
      throw new RuntimeException("Invalid refresh token");
    }

    if (tokenEntity.getExpiryDate().before(new Date())) {
      throw new RuntimeException("Refresh token expired");
    }

    // 새 액세스 토큰 생성
    // 이메일 정보가 필요하면 DB에서 가져와야 함
    String email = ""; // TODO: userId로 이메일 조회
    return jwtProvider.createToken(userId.toString(), email);
  }
}
