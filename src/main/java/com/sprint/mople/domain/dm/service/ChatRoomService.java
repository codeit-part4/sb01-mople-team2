package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import java.util.UUID;

public interface ChatRoomService {
  ChatRoomResponse createChatRoom(UUID requestUserId, UUID targetUserId);

}
