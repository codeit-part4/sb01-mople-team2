package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class AlreadyLikedException extends MopleException {
  public AlreadyLikedException(){
    super(ErrorCode.PLAYLIST_ALREADY_LIKED);
  }
}
