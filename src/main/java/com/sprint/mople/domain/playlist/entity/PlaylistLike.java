package com.sprint.mople.domain.playlist.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
    name = "playlist_likes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "playlist_id"})
)
public class PlaylistLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "playlist_like_id")
  private Long id;

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
