package com.sprint.mople.domain.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.global.jwt.JwtProvider;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtProvider jwtProvider;

    private User testUser;
    String token;
    @BeforeEach
    void setUp() {
        testUser = User.builder().id(UUID.randomUUID()).build();
        token = jwtProvider.createToken(String.valueOf(testUser.getId()), testUser.getEmail());
        SecurityContextHolder
           .getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(testUser, "", List.of())
      );
    }

//    @Test
//    @DisplayName("알림 구독 성공 - GET /api/notifications/subscribe")
//    void subscribe() throws Exception {
//        given(notificationService.subscribe(any(UUID.class), anyString()))
//                .willReturn(new SseEmitter());
//
//        mockMvc.perform(get("/api/notifications/subscribe")
//                        .header("Last-Event-ID", "12345")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType("text/event-stream"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("알림 목록 조회 성공 - GET /api/notifications")
//    void getNotifications() throws Exception {
//        NotificationResponse response = new NotificationResponse(UUID.randomUUID(), "테스트 알림", "/dm/1", false, NotificationType.DM_RECEIVED, LocalDateTime.now());
//        Page<NotificationResponse> responsePage = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);
//
//        given(notificationService.getNotifications(any(User.class), any(PageRequest.class)))
//                .willReturn(responsePage);
//
//        mockMvc.perform(get("/api/notifications")
//                        .header("Authorization", "Bearer " + token)
//                        .param("page", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content[0].content").value("테스트 알림"));
//    }
//
//    @Test
//    @DisplayName("알림 읽음 처리 성공 - PATCH /api/notifications/{notificationId}/read")
//    void readNotification() throws Exception {
//        // Given
//        UUID notificationId = UUID.randomUUID();
//        NotificationResponse response = new NotificationResponse(notificationId, "읽음 처리된 알림", "/dm/1", true, NotificationType.DM_RECEIVED, LocalDateTime.now());
//
//        given(notificationService.readNotification(any(User.class), eq(notificationId)))
//                .willReturn(response);
//
//        // When & Then
//        mockMvc.perform(patch("/api/notifications/{notificationId}/read", notificationId)
//                        .header("Authorization", "Bearer " + token)
//                        .with(csrf())) // 2. 요청에 csrf() 토큰을 추가
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.isRead").value(true));
//    }
}
