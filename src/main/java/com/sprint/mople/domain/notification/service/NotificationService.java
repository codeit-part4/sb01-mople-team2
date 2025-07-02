package com.sprint.mople.domain.notification.service;

import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

public interface NotificationService {

    SseEmitter subscribe(UUID userId, String lastEventId);

    void send(User user, NotificationType notificationType, String content, String relatedUrl);

    Page<NotificationResponse> getNotifications(User user, Pageable pageable);

    NotificationResponse readNotification(User user, UUID notificationId);
}