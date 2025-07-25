package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface ChatRoomService {
  ChatRoomResponse createChatRoom(UUID requestUserId, UUID targetUserId);

  Page<ChatRoomResponse> findAllChatRooms(UUID userId);

}
