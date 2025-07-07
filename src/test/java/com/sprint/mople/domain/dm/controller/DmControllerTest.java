package com.sprint.mople.domain.dm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.service.ChatRoomServiceImpl;
import com.sprint.mople.domain.dm.service.MessageServiceImpl;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class DmControllerTest {

  @Mock
  ChatRoomServiceImpl chatRoomService;

  @Mock
  MessageServiceImpl messageService;

  @Mock
  JwtProvider jwtProvider;

  @Mock
  JwtTokenExtractor jwtTokenExtractor;

  @Mock
  HttpServletRequest httpServletRequest;

  @InjectMocks
  DmController dmController;

  @Test
  void 채팅방_생성_API_성공() {
    // Given
    UUID targetUserId = UUID.randomUUID();
    UUID requestUserId = UUID.randomUUID();
    when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtProvider.getUserId("token")).thenReturn(requestUserId);
    when(chatRoomService.createChatRoom(any(UUID.class), any(UUID.class)))
        .thenReturn(new ChatRoomResponse(targetUserId, List.of(requestUserId, targetUserId)));

    // When
    ResponseEntity<ChatRoomResponse> chatRoomResponse = dmController.createChatRoom(requestUserId, httpServletRequest);

    // Then
    ChatRoomResponse body = chatRoomResponse.getBody();
    assert body != null;
    assert(body.participantIds().contains(requestUserId));
    assert(body.participantIds().contains(targetUserId));
  }

  @Test
  void 메세지_전체조회_API_성공() {
  }

  @Test
  void 채팅방_전체조회_API_성공() {
  }
}