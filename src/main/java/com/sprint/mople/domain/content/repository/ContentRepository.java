package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, UUID>, ContentRepositoryCustom {

  @EntityGraph(attributePaths = {"contentLikes"})
  List<Content> findAllByIdIn(Collection<UUID> ids);

  boolean existsByTitle(String title);

  boolean existsByCategory(Category category);

  boolean existsByExternalId(String externalId);
}
