package com.sprint.mople.domain.review.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;
import java.util.UUID;

public class ReviewNotFoundException extends MopleException {

  public ReviewNotFoundException() {
    super(ErrorCode.REVIEW_NOT_FOUND);
  }

  public ReviewNotFoundException(UUID id) {
    super(ErrorCode.REVIEW_NOT_FOUND);
  }
}

