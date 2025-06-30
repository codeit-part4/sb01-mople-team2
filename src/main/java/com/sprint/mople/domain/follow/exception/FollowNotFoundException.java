package com.sprint.mople.domain.follow.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class FollowNotFoundException extends MopleException {
  public FollowNotFoundException() {
    super(ErrorCode.FOLLOW_NOT_FOUND);
  }
}
