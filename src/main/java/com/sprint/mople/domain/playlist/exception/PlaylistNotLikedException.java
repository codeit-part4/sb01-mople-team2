package com.sprint.mople.domain.playlist.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class PlaylistNotLikedException extends MopleException{
  public PlaylistNotLikedException(){
    super(ErrorCode.PLAYLIST_NOT_LIKED);
  }
}
