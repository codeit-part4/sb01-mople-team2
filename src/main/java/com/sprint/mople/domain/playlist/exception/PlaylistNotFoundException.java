package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class PlaylistNotFoundException extends MopleException {

  public PlaylistNotFoundException() {
    super(ErrorCode.PLAYLIST_NOT_FOUND);
  }
}
