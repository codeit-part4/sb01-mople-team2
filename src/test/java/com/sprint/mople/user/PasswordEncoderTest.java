package com.sprint.mople.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Test
  void 비밀번호_암호화_일치() {
    // given
    String rawPassword = "password123";

    // when
    String encodedPassword = passwordEncoder.encode(rawPassword);

    // then
    assertNotEquals(rawPassword, encodedPassword);
    assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
  }

  @Test
  void 비밀번호_암호화_불일치() {
    // given
    String rawPassword = "password123";
    String wrongPassword = "wrongpass";
    String encodedPassword = passwordEncoder.encode(rawPassword);

    // then
    assertFalse(passwordEncoder.matches(wrongPassword, encodedPassword));
  }
}

