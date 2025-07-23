package com.sprint.mople.domain.dm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageCreateRequest;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.Message;
import com.sprint.mople.domain.dm.mapper.MessageMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.dm.repository.ChatRoomUserRepository;
import com.sprint.mople.domain.dm.repository.MessageRepository;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  ChatRoomUserRepository chatRoomUserRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  ChatRoomService chatRoomService;

  @Mock
  ApplicationEventPublisher eventPublisher;

  @Mock
  MessageMapper messageMapper;

  @Mock
  NotificationService notificationService;

  @InjectMocks
  MessageServiceImpl messageService;

  @Test
  void 메세지_생성_성공() {
    // Given
    UUID senderId = UUID.randomUUID();
    UUID receiverId = UUID.randomUUID();
    String content = "Test Message";
    User sender = new User();
    ChatRoom chatRoom = new ChatRoom(new User(), new User());
    Message message = new Message(chatRoom, sender, content);
    MessageCreateRequest request = new MessageCreateRequest(senderId, receiverId, content);

    when(chatRoomUserRepository.findChatRoomByUserIds(senderId, receiverId))
        .thenReturn(Optional.of(chatRoom));
    when(userRepository.findById(any())).thenReturn(Optional.of(sender));
    when(messageRepository.save(any(Message.class))).thenReturn(message);
    when(messageMapper.toDto(any(Message.class))).thenReturn(
        new MessageResponse(UUID.randomUUID(), UUID.randomUUID(), senderId, content,
            Instant.now()));

    // When
    MessageResponse response = messageService.create(request);

    // Then
    assertNotNull(response);
    assertEquals(senderId, response.senderId());
    assertEquals(content, response.content());
  }

  @Test
  void 메세지_생성_채팅방_존재하지_않는_경우_채팅방_생성() {
    // Given
    UUID senderId = UUID.randomUUID();
    UUID receiverId = UUID.randomUUID();
    String content = "Test Message";
    User sender = new User();
    ChatRoom chatRoom = new ChatRoom(new User(), new User());
    Message message = new Message(chatRoom, sender, content);
    MessageCreateRequest request = new MessageCreateRequest(senderId, receiverId, content);

    when(chatRoomUserRepository.findChatRoomByUserIds(senderId, receiverId))
        .thenReturn(Optional.empty());
    when(userRepository.findById(any())).thenReturn(Optional.of(sender));
    when(messageRepository.save(any(Message.class))).thenReturn(message);
    when(messageMapper.toDto(any(Message.class))).thenReturn(
        new MessageResponse(UUID.randomUUID(), UUID.randomUUID(), senderId, content,
            Instant.now()));
    when(chatRoomService.createChatRoom(senderId, receiverId)).thenReturn(
        new ChatRoomResponse(UUID.randomUUID(), List.of(senderId, receiverId)));
    when(chatRoomRepository.findById(any())).thenReturn(Optional.of(chatRoom));

    // When
    MessageResponse response = messageService.create(request);

    // Then
    assertNotNull(response);
    assertEquals(senderId, response.senderId());
    assertEquals(content, response.content());
  }

  @Test
  void DM_내용_조회_성공() {
    // Given
    UUID requestUserId = UUID.randomUUID();
    UUID targetUserId = UUID.randomUUID();
    UUID chatRoomId = UUID.randomUUID();
    User requestUser = new User();
    ChatRoom chatRoom = new ChatRoom(new User(), new User());
    MessageResponse returnedResponse = new MessageResponse(UUID.randomUUID(), UUID.randomUUID(),
        requestUserId, "Test Message",
        Instant.now());

    ReflectionTestUtils.setField(chatRoom, "id", chatRoomId);
    when(chatRoomUserRepository.findChatRoomByUserIds(requestUserId, targetUserId)).thenReturn(
        Optional.of(chatRoom));

    when(messageMapper.toDto(any())).thenReturn(returnedResponse);
    when(messageRepository.findAllByChatRoomId(chatRoomId)).thenReturn(
        List.of(new Message(chatRoom, requestUser, "Test Message")));

    // When
    List<MessageResponse> response = messageService.findAll(requestUserId, targetUserId);

    // Then
    assertNotNull(response);
    assertEquals("Test Message", response.get(0).content());
  }
}