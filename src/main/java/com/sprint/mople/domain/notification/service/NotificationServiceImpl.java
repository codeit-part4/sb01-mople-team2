package com.sprint.mople.domain.notification.service;

import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.entity.Notification;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.notification.exception.NotificationForbiddenAccessException;
import com.sprint.mople.domain.notification.exception.NotificationNotFoundException;
import com.sprint.mople.domain.notification.repository.EmitterRepository;
import com.sprint.mople.domain.notification.repository.NotificationRepository;
import com.sprint.mople.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public SseEmitter subscribe(UUID userId, String lastEventId) {
        String emitterId = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        sendToEmitter(emitter, emitterId, "EventStream Created. [userId=" + userId + "]");

        if (lastEventId != null && !lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToEmitter(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    @Override
    @Transactional
    public void send(User user, NotificationType notificationType, String content, String relatedUrl) {
        Notification notification = createNotification(user, notificationType, content, relatedUrl);
        notificationRepository.save(notification);

        String userId = String.valueOf(user.getId());
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        emitters.forEach(
                (emitterId, emitter) -> {
                    NotificationResponse response = NotificationResponse.from(notification);
                    emitterRepository.saveEventCache(emitterId, response);
                    sendToEmitter(emitter, emitterId, response);
                }
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotifications(User user, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return notifications.map(NotificationResponse::from);
    }

    @Override
    @Transactional
    public NotificationResponse readNotification(User user, UUID notificationId) {
        // --- 수정된 부분 1 ---
        // orElseThrow에 새 예외 클래스(메서드 참조 형식)를 적용합니다.
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationNotFoundException::new);

        // --- 수정된 부분 2 ---
        // 조건에 맞지 않을 경우, 명확한 이름의 예외를 발생시킵니다.
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new NotificationForbiddenAccessException();
        }

        notification.read();
        return NotificationResponse.from(notification);
    }

    private Notification createNotification(User user, NotificationType type, String content, String url) {
        return Notification.builder()
                .user(user)
                .type(type)
                .content(content)
                .relatedUrl(url)
                .build();
    }

    private void sendToEmitter(SseEmitter emitter, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("sse")
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
        }
    }
}