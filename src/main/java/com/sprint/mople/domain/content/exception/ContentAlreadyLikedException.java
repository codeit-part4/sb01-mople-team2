package com.sprint.mople.domain.content.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class ContentAlreadyLikedException extends MopleException {

  public ContentAlreadyLikedException() {
    super(ErrorCode.CONTENT_NOT_LIKED);
  }
}
