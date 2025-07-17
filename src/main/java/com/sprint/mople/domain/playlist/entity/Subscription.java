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
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "subscribes")
@Getter
@NoArgsConstructor
public class Subscription {

  @Id
  @Column(name = "subscribe_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id")
  private Playlist playlist;

  @CreationTimestamp
  @Column(
      name = "subscribed_at",
      columnDefinition = "timestamp with time zone",
      nullable = false,
      updatable = false
  )
  private Instant subscribedAt;

  public void setPlaylist(Playlist playlist) {
    if (this.playlist != null) {
      this.playlist
          .getSubscriptions()
          .remove(this);
    }
    this.playlist = playlist;
    if (playlist != null) {
      playlist
          .getSubscriptions()
          .add(this);
    }
  }
}

