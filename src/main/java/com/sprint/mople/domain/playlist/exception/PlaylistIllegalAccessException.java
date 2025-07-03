package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class PlaylistIllegalAccessException extends MopleException {

  public PlaylistIllegalAccessException() {
    super(ErrorCode.PLAYLIST_ILLEGAL_ACCESS);
  }
}
