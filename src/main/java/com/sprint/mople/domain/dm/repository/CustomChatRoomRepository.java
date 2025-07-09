package com.sprint.mople.domain.dm.repository;

import com.sprint.mople.domain.dm.entity.ChatRoom;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomChatRoomRepository {
  Page<ChatRoom> findAllByUserIdWithParticipants(UUID userId, Pageable pageable);

}
