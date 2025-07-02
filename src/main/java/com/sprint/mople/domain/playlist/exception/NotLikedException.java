package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class NotLikedException extends MopleException{
  public NotLikedException(){
    super(ErrorCode.PLAYLIST_NOT_LIKED);
  }
}
