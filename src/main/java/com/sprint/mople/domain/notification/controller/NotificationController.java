package com.sprint.mople.domain.notification.controller;

import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

/**
 * 알림 기능의 API 엔드포인트를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(
            @AuthenticationPrincipal User user,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(user.getId(), lastEventId);
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal User user, Pageable pageable) {
        Page<NotificationResponse> notifications = notificationService.getNotifications(user, pageable);
        return ResponseEntity.ok(notifications);
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> readNotification(
            @AuthenticationPrincipal User user,
            @PathVariable UUID notificationId) {
        NotificationResponse response = notificationService.readNotification(user, notificationId);
        return ResponseEntity.ok(response);
    }
}