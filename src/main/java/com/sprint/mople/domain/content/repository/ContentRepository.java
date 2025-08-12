package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContentRepository extends JpaRepository<Content, UUID>, ContentRepositoryCustom {

  @EntityGraph(attributePaths = {"contentLikes"})
  List<Content> findAllByIdIn(Collection<UUID> ids);

  boolean existsByTitle(String title);

  boolean existsByCategory(Category category);

  boolean existsByExternalId(String externalId);

  @Query("SELECT DISTINCT c FROM Content c " +
         "LEFT JOIN FETCH c.genres " +
         "LEFT JOIN FETCH c.watchSession ws " +
         "LEFT JOIN FETCH ws.participants " +
         "WHERE (:title IS NULL OR c.normalizedTitle LIKE %:title%) " +
         "AND (:lastValue IS NULL OR c.normalizedTitle > :lastValue OR (c.normalizedTitle = :lastValue AND c.id > :lastId)) " +
         "ORDER BY c.normalizedTitle ASC, c.id ASC")
  List<Content> findContentsWithAllRelations(@Param("title") String title,
      @Param("lastValue") String lastValue,
      @Param("lastId") UUID lastId,
      Pageable pageable);
}
