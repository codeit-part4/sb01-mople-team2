package com.sprint.mople.domain.follow.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class FollowAlreadyExistsException extends MopleException {

  public FollowAlreadyExistsException() {
    super(ErrorCode.FOLLOW_ALREADY_EXISTS);
  }
  }
