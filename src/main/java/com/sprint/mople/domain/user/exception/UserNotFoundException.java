package com.sprint.mople.domain.user.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class UserNotFoundException extends MopleException {

  public UserNotFoundException() {
    super(ErrorCode.USER_NOT_FOUND);
  }
}
