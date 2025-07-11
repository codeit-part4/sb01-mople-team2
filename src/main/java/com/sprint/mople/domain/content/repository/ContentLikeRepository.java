package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.ContentLike;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentLikeRepository extends JpaRepository<ContentLike, Long> {

  boolean existsByUserAndContent(User user, Content content);

  Optional<ContentLike> findByUserAndContent(User user, Content content);

  long countByContent(Content content);

  List<ContentLike> findByUser(User user);

  boolean existsByUserIdAndContentId(UUID userId, UUID contentId);
}
