package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, UUID> {

  List<Playlist> findAllByIsPublicTrue();

  List<Playlist> findAllByUser(User user);
}
