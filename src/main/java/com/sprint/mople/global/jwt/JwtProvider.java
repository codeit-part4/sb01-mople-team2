package com.sprint.mople.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  @Value("${jwt.secret}")
  private String secret;

  private Key key;
  private final long expirationMillis = 1000 * 60 * 60; // 1시간
  private final long refreshExpirationMillis = 1000 * 60 * 60 * 24 * 7; // 7일

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(secret.getBytes());
  }

  public String createToken(String userId, String email) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMillis);

    return Jwts.builder()
        .setSubject(userId)
        .claim("email", email)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public Claims getClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
  }

  public long getExpirationSeconds() {
    return expirationMillis / 1000;
  }

  public String createRefreshToken(String userId) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + refreshExpirationMillis);

    return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public long getRefreshExpirationMillis() {
    return refreshExpirationMillis;
  }

}

