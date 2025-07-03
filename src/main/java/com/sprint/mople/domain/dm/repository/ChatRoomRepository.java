package com.sprint.mople.domain.dm.repository;

import com.sprint.mople.domain.dm.entity.ChatRoom;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

  @Query(
      "SELECT cr FROM ChatRoom cr "
          + "JOIN cr.participants cru "
          + "WHERE cru.user.id = :userId"
  )
  Page<ChatRoom> findAllByParticipantId(UUID userId, Pageable pageable);
}
