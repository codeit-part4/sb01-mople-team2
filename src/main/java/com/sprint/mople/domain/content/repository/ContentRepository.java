package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, UUID> {

  Optional<Content> findByExternalIdAndSource(String externalId, Content.Source source);

  @EntityGraph(attributePaths = {"contentLikes"})
  List<Content> findAllByIdIn(Collection<UUID> ids);
}
