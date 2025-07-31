package com.sprint.mople.domain.content.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class ContentNotFoundException extends MopleException {

  public ContentNotFoundException() {
    super(ErrorCode.CONTENT_NOT_FOUND);
  }
}
