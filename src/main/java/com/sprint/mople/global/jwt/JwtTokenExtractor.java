package com.sprint.mople.global.jwt;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;

public class JwtTokenExtractor {

  public static String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

  public static UUID extractUserId(HttpServletRequest request, JwtProvider jwtProvider){
    String token = resolveToken(request);
    if (token == null) {
      throw new IllegalArgumentException("JWT 토큰이 없습니다.");
    }
    return jwtProvider.getUserId(token);
  }

}
