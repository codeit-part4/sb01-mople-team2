package com.sprint.mople.domain.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mople.domain.notification.dto.NotificationResponse;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf; // 1. csrf import 추가
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false) // Spring Security 필터를 끔
@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(testUser, "", List.of())
        );
    }

    @Test
    @DisplayName("알림 구독 성공 - GET /api/notifications/subscribe")
    void subscribe() throws Exception {
        given(notificationService.subscribe(any(UUID.class), anyString()))
                .willReturn(new SseEmitter());

        mockMvc.perform(get("/api/notifications/subscribe")
                        .header("Last-Event-ID", "12345")
                        .contentType("text/event-stream"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("알림 목록 조회 성공 - GET /api/notifications")
    void getNotifications() throws Exception {
        NotificationResponse response = new NotificationResponse(UUID.randomUUID(), "테스트 알림", "/dm/1", false, NotificationType.DM_RECEIVED, LocalDateTime.now());
        Page<NotificationResponse> responsePage = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

        given(notificationService.getNotifications(any(User.class), any(PageRequest.class)))
                .willReturn(responsePage);

        mockMvc.perform(get("/api/notifications")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].content").value("테스트 알림"));
    }

    @Test
    @DisplayName("알림 읽음 처리 성공 - PATCH /api/notifications/{notificationId}/read")
    void readNotification() throws Exception {
        // Given
        UUID notificationId = UUID.randomUUID();
        NotificationResponse response = new NotificationResponse(notificationId, "읽음 처리된 알림", "/dm/1", true, NotificationType.DM_RECEIVED, LocalDateTime.now());

        given(notificationService.readNotification(any(User.class), eq(notificationId)))
                .willReturn(response);

        // When & Then
        mockMvc.perform(patch("/api/notifications/{notificationId}/read", notificationId)
                        .with(csrf())) // 2. 요청에 csrf() 토큰을 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isRead").value(true));
    }
}
