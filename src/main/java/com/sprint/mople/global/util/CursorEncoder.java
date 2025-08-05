package com.sprint.mople.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mople.global.dto.Cursor;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CursorEncoder {

  private final ObjectMapper objectMapper;

  public Cursor decode(String encodedCursor) {
    if (encodedCursor == null) return new Cursor(null, null);
    try {
      byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedCursor);
      String json = new String(decodedBytes, StandardCharsets.UTF_8);
      return objectMapper.readValue(json, Cursor.class);
    } catch (Exception e) {
      log.warn("커서 디코딩 실패: {}", encodedCursor, e);
      return new Cursor(null, null);
    }
  }

  public String encode(String value, UUID id) {
    try {
      Cursor cursor = new Cursor(value, id.toString());
      String json = objectMapper.writeValueAsString(cursor);
      return Base64.getUrlEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    } catch (Exception e) {
      log.error("커서 인코딩 중 오류 발생", e);
      throw new RuntimeException(e);
    }
  }
}