package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class PlaylistContentNotFoundException extends MopleException {

  public PlaylistContentNotFoundException() {
    super(ErrorCode.PLAYLIST_CONTENT_NOT_FOUND);
  }
}
