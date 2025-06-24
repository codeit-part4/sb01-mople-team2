package com.sprint.mople.global.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

  private static final String SPLIT_CHAR = ",";

  @Override
  public String convertToDatabaseColumn(Set<String> attribute) {
    if (attribute == null || attribute.isEmpty()) {
      return "";
    }
    return attribute.stream()
        .collect(Collectors.joining(SPLIT_CHAR));
  }

  @Override
  public Set<String> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return Collections.emptySet();
    }
    return Arrays.stream(dbData.split(SPLIT_CHAR))
        .map(String::trim)
        .collect(Collectors.toSet());
  }
}

