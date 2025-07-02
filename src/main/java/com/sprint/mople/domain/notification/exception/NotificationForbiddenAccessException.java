// --- com/sprint/mople/domain/notification/exception/NotificationForbiddenAccessException.java ---

package com.sprint.mople.domain.notification.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class NotificationForbiddenAccessException extends MopleException {

  public NotificationForbiddenAccessException() {
    super(ErrorCode.FORBIDDEN_ACCESS);
  }
}