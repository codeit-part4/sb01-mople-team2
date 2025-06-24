package com.sprint.mople.domain.watchsession.repository;

import com.sprint.mople.domain.watchsession.entity.WatchComment;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchCommentRepository extends JpaRepository<WatchComment, UUID> {

  List<WatchComment> findAllBySessionIdOrderBySentAtAsc(UUID sessionId);
}
