package com.sprint.mople.global.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class TitleNormalizer {

  public static String normalize(String input) {
    if (input == null || input.isEmpty()) {
      return "";
    }

    String normalized = Normalizer.normalize(input, Form.NFD);

    normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

    normalized = Normalizer.normalize(normalized, Form.NFC);

    normalized = normalized.toLowerCase();

    normalized = normalized.replaceAll("[^a-z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\p{IsHan}]", "");

    return normalized;
  }
}
