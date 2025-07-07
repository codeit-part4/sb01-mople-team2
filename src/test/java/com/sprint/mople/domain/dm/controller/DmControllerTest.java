package com.sprint.mople.domain.dm.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.service.ChatRoomServiceImpl;
import com.sprint.mople.domain.dm.service.MessageServiceImpl;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    // Given
    UUID targetUserId = UUID.randomUUID();
    UUID requestUserId = UUID.randomUUID();
    UUID chatRoomId = UUID.randomUUID();
    MessageResponse messageResponse = new MessageResponse(
        UUID.randomUUID(), chatRoomId, requestUserId, "Hello", Instant.now());
    when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtProvider.getUserId("token")).thenReturn(requestUserId);
    when(messageService.findAll(requestUserId, targetUserId)).thenReturn(List.of(messageResponse));

    // When
    ResponseEntity<List<MessageResponse>> response = dmController.findAllMessages(targetUserId, httpServletRequest);

    // Then
    List<MessageResponse> body = response.getBody();
    assert body != null;
    assert body.size() == 1;
    assert body.get(0).content().equals("Hello");
  }

  @Test
  void 채팅방_전체조회_API_성공() {
    // Given
    UUID userId = UUID.randomUUID();
    ChatRoomResponse chatRoomResponse = new ChatRoomResponse(UUID.randomUUID(), List.of(userId));
    List<ChatRoomResponse> chatRoomResponses = List.of(chatRoomResponse);
    Page<ChatRoomResponse> chatRoomResponsePage = new PageImpl<>(chatRoomResponses, PageRequest.of(0, 10), chatRoomResponses.size());
    when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtProvider.getUserId("token")).thenReturn(userId);

    when(chatRoomService.findAllChatRooms(userId)).thenReturn(chatRoomResponsePage);
    // When
    ResponseEntity<Page<ChatRoomResponse>> response = dmController.findAllChatRooms(httpServletRequest);

    // Then
    Page<ChatRoomResponse> body = response.getBody();
    assert body != null;
    assert body.getTotalElements() == 1;
    assert body.getContent().get(0).participantIds().contains(userId);
  }
}