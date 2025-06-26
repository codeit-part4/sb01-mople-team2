package com.sprint.mople.domain.notification.dto;

import com.sprint.mople.domain.notification.entity.Notification;
import com.sprint.mople.domain.notification.entity.NotificationType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @param notificationId   알림의 고유 ID
 * @param content          알림 내용
 * @param relatedUrl       관련 리소스 URL
 * @param isRead           읽음 여부
 * @param notificationType 알림 종류
 * @param createdAt        알림 생성 시각
 */
public record NotificationResponse(
        UUID notificationId,
        String content,
        String relatedUrl,
        boolean isRead,
        NotificationType notificationType,
        LocalDateTime createdAt
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getContent(),
                notification.getRelatedUrl(),
                notification.isRead(),
                notification.getType(),
                notification.getCreatedAt()
        );
    }
}