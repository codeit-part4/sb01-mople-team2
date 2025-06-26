package com.sprint.mople.domain.dm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.Message;
import com.sprint.mople.domain.dm.mapper.MessageMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.dm.repository.ChatRoomUserRepository;
import com.sprint.mople.domain.dm.repository.MessageRepository;
import com.sprint.mople.domain.user.entity.User;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  ChatRoomUserRepository chatRoomUserRepository;

  @Mock
  MessageMapper messageMapper;

  @InjectMocks
  MessageServiceImpl messageService;

  @Test
  void create() {
    // Given
    UUID senderId = UUID.randomUUID();
    String content = "Test Message";
    User sender = new User();
    ChatRoom chatRoom = new ChatRoom(new User(), new User());
    Message message = new Message(chatRoom, sender, content);

    when(messageRepository.save(any(Message.class))).thenReturn(message);

    when(messageMapper.toDto(any(Message.class))).thenReturn(
        new MessageResponse(UUID.randomUUID(), UUID.randomUUID(), senderId, content,
            Instant.now()));

    // When
    MessageResponse response = messageService.create(senderId, content);

    // Then
    assertNotNull(response);
    assertEquals(senderId, response.senderId());
    assertEquals(content, response.content());
  }

  @Test
  void DM_내용_조회_성공() throws Exception {
    // Given
    UUID requestUserId = UUID.randomUUID();
    UUID targetUserId = UUID.randomUUID();
    UUID chatRoomId = UUID.randomUUID();
    User requestUser = new User();
    User targetUser = new User();
    ChatRoom chatRoom = new ChatRoom(requestUser, targetUser);

    Field idField = ChatRoom.class.getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(chatRoom, chatRoomId);

    MessageResponse returnedResponse = new MessageResponse(UUID.randomUUID(), UUID.randomUUID(),
        requestUserId, "Test Message",
        Instant.now());

    when(chatRoomUserRepository.findChatRoomIdByUserIds(requestUserId, targetUserId)).thenReturn(
        Optional.of(chatRoomId));
    when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

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