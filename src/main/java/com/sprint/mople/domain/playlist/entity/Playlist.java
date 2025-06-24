package com.sprint.mople.domain.playlist.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Table(name = "playlists")
@NoArgsConstructor
public class Playlist {

  @Id
  @Column(name = "playlist_id", columnDefinition = "uuid")
  @GeneratedValue
  private UUID id;

  // --- ManyToOne: User ---
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  // --- ManyToOne: Subscribe ---
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscribe_id")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private Subscribe subscribe;

  @Column(name = "title", length = 255)
  private String title;

  @Column(name = "description", length = 1000)
  private String description;

  @Column(name = "is_public")
  private Boolean isPublic;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "timestamp with time zone", updatable = false)
  private OffsetDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
  private OffsetDateTime updatedAt;
}