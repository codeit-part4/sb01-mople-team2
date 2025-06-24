package com.sprint.mople.domain.dm.repository;

import com.sprint.mople.domain.dm.entity.ChatRoom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

}
