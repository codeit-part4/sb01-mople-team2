package com.sprint.mople.domain.watchsession.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class SessionAlreadyJoinedException extends MopleException {

  public SessionAlreadyJoinedException() {
    super(ErrorCode.WATCH_SESSION_NOT_FOUND);
  }
}
