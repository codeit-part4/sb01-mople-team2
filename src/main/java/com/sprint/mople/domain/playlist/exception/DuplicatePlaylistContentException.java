package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class DuplicatePlaylistContentException extends MopleException {

  public DuplicatePlaylistContentException() {
    super(ErrorCode.DUPLICATE_PLAYLIST_CONTENT);
  }
}
