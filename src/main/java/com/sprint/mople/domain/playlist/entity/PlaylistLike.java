package com.sprint.mople.domain.playlist.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
    name = "playlist_like",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "playlist_id"})
)
public class PlaylistLike {

  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "playlist_id")
  private Playlist playlist;

  @Column(name = "liked_at", nullable = false)
  private Instant likedAt = Instant.now();

  protected PlaylistLike() { }           // JPA 기본 생성자

  public PlaylistLike(User user, Playlist playlist) {
    this.user = user;
    this.playlist = playlist;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlaylistLike other)) return false;
    return Objects.equals(user, other.user) &&
           Objects.equals(playlist, other.playlist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, playlist);
  }
}
