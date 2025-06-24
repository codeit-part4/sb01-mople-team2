package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, UUID> {

  Optional<Content> findByExternalIdAndSource(String externalId, Content.Source source);


}
