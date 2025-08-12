package com.sprint.mople.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.HashSet;
import java.util.Set;

@Converter
public class JsonbStringSetConverter implements AttributeConverter<Set<String>, String> {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    try {
      return attribute != null ? objectMapper.writeValueAsString(attribute) : "[]";
    } catch (Exception e) {
      throw new IllegalArgumentException("Set<String> -> JSON 변환 실패", e);
    }
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    try {
      return dbData != null ? objectMapper.readValue(dbData, new TypeReference<Set<String>>() {}) : new HashSet<>();
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON -> Set<String> 변환 실패", e);
    }
  }
}
