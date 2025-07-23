package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class PlaylistAlreadyLikedException extends MopleException {
  public PlaylistAlreadyLikedException(){
    super(ErrorCode.PLAYLIST_ALREADY_LIKED);
  }
}
