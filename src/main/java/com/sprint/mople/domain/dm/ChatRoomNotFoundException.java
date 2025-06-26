package com.sprint.mople.domain.dm;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class ChatRoomNotFoundException extends MopleException {

  public ChatRoomNotFoundException() {
    super(ErrorCode.CHAT_ROOM_NOT_FOUND);
  }
}
