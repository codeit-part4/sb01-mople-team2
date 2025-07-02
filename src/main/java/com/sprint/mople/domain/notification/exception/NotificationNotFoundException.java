// --- com/sprint/mople/domain/notification/exception/NotificationNotFoundException.java ---

package com.sprint.mople.domain.notification.exception;

import com.sprint.mople.global.exception.ErrorCode;
import com.sprint.mople.global.exception.MopleException;

public class NotificationNotFoundException extends MopleException {

  public NotificationNotFoundException() {
    super(ErrorCode.NOTIFICATION_NOT_FOUND);
  }
}