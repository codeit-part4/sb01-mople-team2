package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistLike;
import com.sprint.mople.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistLikeRepository extends JpaRepository<PlaylistLike, UUID> {

  boolean existsByUserAndPlaylist(User user, Playlist playlist);

  void deleteByUserAndPlaylist(User user, Playlist playlist);

  long countByPlaylist(Playlist playlist);

  Optional<PlaylistLike> findByUserAndPlaylist(User user, Playlist playlist);
}
