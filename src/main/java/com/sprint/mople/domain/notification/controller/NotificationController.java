package com.sprint.mople.domain.notification.controller;

import static com.sprint.mople.global.jwt.JwtTokenExtractor.extractUserId;

import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.NotFoundException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * 알림 기능의 API 엔드포인트를 제공하는 컨트롤러.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;

  @GetMapping(value = "/subscribe", produces = "text/event-stream")
  public SseEmitter subscribe(
      HttpServletRequest request,
      @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
    UUID followerId = extractUserId(request, jwtProvider);
    return notificationService.subscribe(followerId, lastEventId);
  }

  @GetMapping
  public ResponseEntity<Page<NotificationResponse>> getNotifications(
      HttpServletRequest request, Pageable pageable) {

    UUID followerId = extractUserId(request, jwtProvider);
    User user = userRepository.findById(followerId)
        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

    Page<NotificationResponse> notifications = notificationService.getNotifications(user, pageable);
    return ResponseEntity.ok(notifications);
  }

  @PatchMapping("/{notificationId}/read")
  public ResponseEntity<NotificationResponse> readNotification(
      HttpServletRequest request,
      @PathVariable UUID notificationId) {

    UUID followerId = extractUserId(request, jwtProvider);
    User user = userRepository.findById(followerId)
        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

    NotificationResponse response = notificationService.readNotification(user, notificationId);
    return ResponseEntity.ok(response);
  }
}