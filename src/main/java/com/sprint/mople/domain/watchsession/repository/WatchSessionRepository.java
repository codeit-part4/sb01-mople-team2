package com.sprint.mople.domain.watchsession.repository;

import com.sprint.mople.domain.watchsession.entity.WatchSession;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchSessionRepository extends JpaRepository<WatchSession, UUID> {

  Optional<WatchSession> findByContentId(UUID contentId);

  boolean existsByContentId(UUID contentId);
}
