package com.sprint.mople.domain.dm.repository;

import com.sprint.mople.domain.dm.entity.ChatRoomUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, UUID> {

  @Query("""
          SELECT cu.chatRoom.id FROM ChatRoomUser cu
          WHERE cu.user.id IN (:userId1, :userId2)
          GROUP BY cu.chatRoom.id
          HAVING COUNT(DISTINCT cu.user.id) = 2
      """)
  Optional<UUID> findChatRoomIdByUserIds(@Param("userId1") UUID requestUserId,
      @Param("userId2") UUID targetUserId);
}
