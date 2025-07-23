package com.sprint.mople.domain.content.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class ContentNotLikedException extends MopleException {

  public ContentNotLikedException() {
    super(ErrorCode.CONTENT_NOT_LIKED);
  }
}
