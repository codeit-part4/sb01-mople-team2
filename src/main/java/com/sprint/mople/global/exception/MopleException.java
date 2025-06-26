package com.sprint.mople.global.exception;

import lombok.Getter;

@Getter
public abstract class MopleException extends RuntimeException {

  private final ErrorCode errorCode;

  protected MopleException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
