package com.sprint.mople.global.exception;

import java.time.Instant;
import lombok.Builder;

public record ErrorResponse(
    int status,
    String message,
    String code,
    Instant timestamp
)
{

  @Builder
  public ErrorResponse(int status, String message, String code, Instant timestamp) {
    this.status = status;
    this.message = message;
    this.code = code;
    this.timestamp = timestamp == null ? Instant.now() : timestamp;
  }

  public static ErrorResponse of(ErrorCode errorCode) {
    return ErrorResponse
        .builder()
        .status(errorCode.getStatus())
        .message(errorCode.getMessage())
        .code(errorCode.name())
        .timestamp(Instant.now())
        .build();
  }

  public static ErrorResponse of(ErrorCode errorCode, String customMessage) {
    return ErrorResponse
        .builder()
        .status(errorCode.getStatus())
        .message(customMessage)
        .code(errorCode.name())
        .timestamp(Instant.now())
        .build();
  }
}
