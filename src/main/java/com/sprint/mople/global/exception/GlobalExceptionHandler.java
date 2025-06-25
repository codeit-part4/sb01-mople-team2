package com.sprint.mople.global.exception;

import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MopleException.class)
  public ResponseEntity<ErrorResponse> handleMopleException(MopleException ex) {
    ErrorCode ec = ex.getErrorCode();
    ErrorResponse body = new ErrorResponse(ec.getStatus(), ec.getMessage(), ec.getCode(), Instant.now());
    return ResponseEntity.status(ec.getStatus()).body(body);
  }
}
