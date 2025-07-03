package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.Playlist;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {
}
