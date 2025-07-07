package com.sprint.mople.domain.user.service;

import com.sprint.mople.domain.user.entity.RefreshToken;
import com.sprint.mople.domain.user.exception.UnauthorizedException;
import com.sprint.mople.domain.user.repository.RefreshTokenRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  private final JwtProvider jwtProvider;
  private final RefreshTokenRepository refreshTokenRepository;

  public TokenServiceImpl(JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository) {
    this.jwtProvider = jwtProvider;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public Map<String, String> generateTokens(UUID userId, String email) {
    String accessToken = jwtProvider.createToken(userId.toString(), email);
    String refreshToken = jwtProvider.createRefreshToken(userId.toString());

    RefreshToken tokenEntity = refreshTokenRepository.findByUserId(userId)
        .orElseGet(() -> {
          RefreshToken t = new RefreshToken();
          t.setId(UUID.randomUUID()); // ✅ ID 수동 설정
          return t;
        });

    tokenEntity.setUserId(userId);
    tokenEntity.setToken(refreshToken);
    tokenEntity.setExpiryDate(
        new Date(System.currentTimeMillis() + jwtProvider.getRefreshExpirationMillis()));

    refreshTokenRepository.save(tokenEntity);

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);
    return tokens;
  }

  @Override
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

  @Override
  public Map<String, String> reissueTokens(String refreshToken, HttpServletResponse response) {
    Claims claims;
    try {
      claims = jwtProvider.getClaims(refreshToken);
    } catch (Exception e) {
      throw new UnauthorizedException("유효하지 않은 Refresh Token입니다.");
    }

    UUID userId = UUID.fromString(claims.getSubject());

    RefreshToken stored = refreshTokenRepository.findByUserId(userId)
        .orElseThrow(() -> new UnauthorizedException("Refresh Token이 존재하지 않습니다."));

    if (!stored.getToken().equals(refreshToken)) {
      throw new UnauthorizedException("Refresh Token이 일치하지 않습니다.");
    }

    if (stored.getExpiryDate().before(new Date())) {
      throw new UnauthorizedException("Refresh Token이 만료되었습니다.");
    }

    String newAccessToken = jwtProvider.createToken(userId.toString(),
        claims.get("email", String.class));
    String newRefreshToken = jwtProvider.createRefreshToken(userId.toString());

    // DB 갱신
    stored.setToken(newRefreshToken);
    stored.setExpiryDate(
        new Date(System.currentTimeMillis() + jwtProvider.getRefreshExpirationMillis()));
    refreshTokenRepository.save(stored);

    // 새 쿠키 설정
    ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
        .httpOnly(true)
        .secure(true)
        .path("/api/auth/refresh")
        .sameSite("Strict")
        .maxAge(jwtProvider.getRefreshExpirationSeconds())
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    Map<String, String> result = new HashMap<>();
    result.put("accessToken", newAccessToken);
    result.put("tokenType", "Bearer");
    result.put("expiresIn", String.valueOf(jwtProvider.getAccessExpirationSeconds()));

    return result;
  }

  @Override
  public void logout(String refreshToken, HttpServletResponse response) {
    try {
      Claims claims = jwtProvider.getClaims(refreshToken);
      UUID userId = UUID.fromString(claims.getSubject());

      refreshTokenRepository.findByUserId(userId).ifPresent(refreshTokenRepository::delete);
    } catch (Exception ignored) {
      // 토큰 파싱 실패 or 없는 토큰이면 무시 (안전한 로그아웃 보장)
    }

    // 쿠키 삭제
    ResponseCookie expiredCookie = ResponseCookie.from("refresh_token", "")
        .httpOnly(true)
        .secure(true)
        .path("/api/auth/refresh")
        .maxAge(0) // 즉시 만료
        .sameSite("Strict")
        .build();
    response.addHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
  }

}
