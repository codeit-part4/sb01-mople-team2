package com.sprint.mople.global.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class TempPasswordUtil {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
  private static final int PASSWORD_LENGTH = 10;

  public String generate() {
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < PASSWORD_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      sb.append(CHARACTERS.charAt(index));
    }

    return sb.toString();
  }
}
