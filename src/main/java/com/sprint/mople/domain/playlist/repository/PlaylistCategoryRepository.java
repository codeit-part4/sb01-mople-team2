package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistCategoryRepository extends JpaRepository<PlaylistCategory, Long> {
}
