package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  boolean existsByUserAndPlaylist(User user, Playlist playlist);

  List<Subscription> findAllByUser(User user);

  List<Subscription> findAllByPlaylist(Playlist playlist);

  long countByPlaylist(Playlist playlist);

  long countByUser(User user);

  Optional<Object> findAllByUserAndPlaylist(User user, Playlist playlist);
}
