package com.sprint.mople.user;

import com.sprint.mople.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class JwtProviderTest {

  private JwtProvider jwtProvider;

  @BeforeEach
  public void setup() throws Exception{
    jwtProvider = new JwtProvider();

    Field secretField = JwtProvider.class.getDeclaredField("secret");
    secretField.setAccessible(true);
    secretField.set(jwtProvider, "abcdefghijklmnopqrstuvwxyz1234567890abcdefghij");

    jwtProvider.init();
  }

  @Test
  public void 토큰_유효성_검사() {
    String token = jwtProvider.createToken("user123", "test@example.com");
    assertThat(token).isNotNull();

    Claims claims = jwtProvider.getClaims(token);
    assertThat(claims.getSubject()).isEqualTo("user123");
    assertThat(claims.get("email")).isEqualTo("test@example.com");
  }

  @Test
  public void refresh_토큰_발급() {
    String refreshToken = jwtProvider.createRefreshToken("user123");
    assertThat(refreshToken).isNotNull();

    Claims claims = jwtProvider.getClaims(refreshToken);
    assertThat(claims.getSubject()).isEqualTo("user123");
  }
}

