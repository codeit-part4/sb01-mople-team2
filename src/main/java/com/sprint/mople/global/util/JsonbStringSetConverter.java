package com.sprint.mople.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Set;

@Converter
public class JsonbStringSetConverter implements AttributeConverter<Set<String>, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Exception e) {
      throw new IllegalArgumentException("Set<String>을(를) JSON으로 변환하는 데 실패했습니다.", e);
    }
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    try {
      return objectMapper.readValue(dbData, new TypeReference<>() {});
    } catch (Exception e) {
      throw new IllegalArgumentException("JSON 데이터를 Set<String>으로 변환하는 데 실패했습니다.", e);
    }
  }
}
