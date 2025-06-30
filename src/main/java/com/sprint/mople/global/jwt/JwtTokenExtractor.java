package com.sprint.mople.global.jwt;

import jakarta.servlet.http.HttpServletRequest;

public class JwtTokenExtractor {

  public static String resolveToken(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      return bearer.substring(7);
    }
    return null;
  }

}
