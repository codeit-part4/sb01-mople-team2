package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import com.sprint.mople.domain.playlist.entity.PlaylistCategoryMapping;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistCategoryMappingRepository extends JpaRepository<PlaylistCategoryMapping, Long> {

  boolean existsByPlaylistAndCategory(Playlist playlist, PlaylistCategory category);

  Optional<PlaylistCategoryMapping> findByPlaylistAndCategory(Playlist playlist, PlaylistCategory category);
}
