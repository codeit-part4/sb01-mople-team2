package com.sprint.mople.notification;

import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.entity.Notification;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.notification.exception.NotificationForbiddenAccessException;
import com.sprint.mople.domain.notification.exception.NotificationNotFoundException;
import com.sprint.mople.domain.notification.repository.EmitterRepository;
import com.sprint.mople.domain.notification.repository.NotificationRepository;
import com.sprint.mople.domain.notification.service.NotificationServiceImpl;
import com.sprint.mople.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmitterRepository emitterRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    @DisplayName("알림 구독 성공")
    void subscribe_성공() {
        // Given
        UUID userId = UUID.randomUUID();
        String lastEventId = "";
        when(emitterRepository.save(anyString(), any(SseEmitter.class)))
                .thenReturn(new SseEmitter());

        // When
        SseEmitter emitter = notificationService.subscribe(userId, lastEventId);

        // Then
        assertNotNull(emitter);
        verify(emitterRepository, times(1)).save(anyString(), any(SseEmitter.class));
    }

    @Test
    @DisplayName("알림 전송 성공")
    void send_성공() {
        // Given
        User user = User.builder().id(UUID.randomUUID()).build();
        NotificationType type = NotificationType.NEW_FOLLOWER;
        String content = "새로운 팔로워가 생겼습니다.";
        String url = "/profile/user123";

        // Mock 객체 설정
        when(emitterRepository.findAllEmitterStartWithByUserId(anyString()))
                .thenReturn(Map.of("emitterId", new SseEmitter()));
        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        notificationService.send(user, type, content, url);

        // Then
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emitterRepository, times(1)).findAllEmitterStartWithByUserId(anyString());
    }

    @Test
    @DisplayName("알림 목록 조회 성공")
    void getNotifications_성공() {
        // Given
        User user = User.builder().id(UUID.randomUUID()).build();
        Pageable pageable = PageRequest.of(0, 10);

        Notification notification = Notification.builder().user(user).content("테스트 알림").type(NotificationType.DM_RECEIVED).build();
        Page<Notification> notificationPage = new PageImpl<>(List.of(notification), pageable, 1);

        when(notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable))
                .thenReturn(notificationPage);

        // When
        Page<NotificationResponse> responsePage = notificationService.getNotifications(user, pageable);

        // Then
        assertNotNull(responsePage);
        assertEquals(1, responsePage.getTotalElements());
        assertEquals("테스트 알림", responsePage.getContent().get(0).content());
    }

    @Test
    @DisplayName("알림 읽음 처리 성공")
    void readNotification_성공() {
        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Notification notification = Notification.builder().user(user).content("읽지 않은 알림").type(NotificationType.DM_RECEIVED).build();
        UUID notificationId = notification.getId();

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        // When
        NotificationResponse response = notificationService.readNotification(user, notificationId);

        // Then
        assertNotNull(response);
        assertTrue(response.isRead());
    }

    @Test
    @DisplayName("알림 읽음 처리 실패 - 존재하지 않는 알림")
    void readNotification_실패_알림_없음() {
        // Given
        User user = User.builder().id(UUID.randomUUID()).build();
        UUID nonExistentNotificationId = UUID.randomUUID();

        when(notificationRepository.findById(nonExistentNotificationId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotificationNotFoundException.class, () -> {
            notificationService.readNotification(user, nonExistentNotificationId);
        });
    }

    @Test
    @DisplayName("알림 읽음 처리 실패 - 권한 없음")
    void readNotification_실패_권한_없음() {
        // Given
        User owner = User.builder().id(UUID.randomUUID()).build();
        User otherUser = User.builder().id(UUID.randomUUID()).build();
        Notification notification = Notification.builder().user(owner).content("주인만 볼 수 있는 알림").type(NotificationType.DM_RECEIVED).build();
        UUID notificationId = notification.getId();

        when(notificationRepository.findById(notificationId))
                .thenReturn(Optional.of(notification));

        // When & Then
        assertThrows(NotificationForbiddenAccessException.class, () -> {
            notificationService.readNotification(otherUser, notificationId);
        });
    }
}