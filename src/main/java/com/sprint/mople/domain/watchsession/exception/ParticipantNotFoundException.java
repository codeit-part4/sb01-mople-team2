package com.sprint.mople.domain.watchsession.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class ParticipantNotFoundException extends MopleException {

  public ParticipantNotFoundException() {
    super(ErrorCode.PARTICIPANT_NOT_FOUND);
  }
}
