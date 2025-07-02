package com.sprint.mople.domain.dm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.mapper.ChatRoomMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceImplTest {

  @Mock
  ChatRoomRepository chatRoomRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  ChatRoomMapper chatRoomMapper;

  @InjectMocks
  ChatRoomServiceImpl chatRoomService;

  @Test
  void 채팅방_생성_성공() {
    // Given
    UUID requestUserId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(requestUserId)).thenReturn(Optional.of(new User()));
    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
    when(chatRoomMapper.toDto(any())).thenReturn(
        new ChatRoomResponse(UUID.randomUUID(), List.of(requestUserId, userId)));

    // When
    ChatRoomResponse response = chatRoomService.createChatRoom(requestUserId, userId);

    // Then
    assert (response.participantIds().contains(requestUserId));
    assert (response.participantIds().contains(userId));

  }

  void 채팅방_목록_조회(){
    // Given
    UUID userId = UUID.randomUUID();
    ChatRoom chatRoom = new ChatRoom(new User(), new User());
    ChatRoomResponse response = new ChatRoomResponse(UUID.randomUUID(), List.of(userId));

    int page = 0;
    int size = 10;

    when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
    when(chatRoomRepository.findAllByParticipantId(userId)).thenReturn(List.of(chatRoom));
    when(chatRoomMapper.toDto(any())).thenReturn(response);

    // When
    Page<ChatRoomResponse> chatRooms = chatRoomService.findAllChatRooms(userId);

    // Then
    assertEquals(1, chatRooms.getTotalElements());
  }
}