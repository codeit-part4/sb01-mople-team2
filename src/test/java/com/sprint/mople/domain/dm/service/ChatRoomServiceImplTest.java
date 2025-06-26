package com.sprint.mople.domain.dm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  ChatRoomServiceImpl chatRoomService;

  @Test
  void 채팅방_생성_성공() {
    // Given
    UUID requestUserId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(requestUserId)).thenReturn(Optional.of(new User()));
    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

    // When
    ChatRoomResponse response = chatRoomService.createChatRoom(requestUserId, userId);

    // Then
    assertNotNull(response);
    assertTrue(response.participantIds().contains(requestUserId));
    assertTrue(response.participantIds().contains(userId));

  }
}