package com.sprint.mople.domain.watchsession.repository;

import com.sprint.mople.domain.watchsession.entity.WatchSessionParticipant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchSessionParticipantRepository extends
    JpaRepository<WatchSessionParticipant, UUID> {

  List<WatchSessionParticipant> findAllBySessionId(UUID sessionId);

  Optional<WatchSessionParticipant> findBySessionIdAndUserId(UUID sessionId, UUID userId);

  boolean existsBySessionIdAndUserId(UUID sessionId, UUID userId);

  int countBySessionId(UUID sessionId);

  Optional<WatchSessionParticipant> findByUserId(UUID userId);

}
