package com.sprint.mople.domain.playlist.entity;

import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
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
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  // --- OneToMany: Subscribe ---
  @OneToMany(
      mappedBy = "playlist",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER
  )
  private final List<Subscribe> subscribes = new ArrayList<>();

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

  // --- OneToMany: PlaylistContent ---
  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<PlaylistContent> playlistContents = new ArrayList<>();

  /**
   * Playlist 생성자
   *
   * @param user        플레이리스트 소유자
   * @param title       플레이리스트 제목
   * @param description 플레이리스트 설명
   * @param isPublic    공개 여부
   */
  @Builder
  public Playlist(User user, String title, String description, Boolean isPublic) {
    this.user = user;
    this.title = title;
    this.description = description;
    this.isPublic = isPublic;
  }

  public void update(PlaylistUpdateRequest request) {
    if (request.title() != null) {
      this.title = request.title();
    }
    if (request.description() != null) {
      this.description = request.description();
    }
    if (request.isPublic() != null) {
      this.isPublic = request.isPublic();
    }
  }

  public void setIsPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

  public void addSubscribe(Subscribe subscribe) {
    subscribes.add(subscribe);
    subscribe.setPlaylist(this);
  }

  public void removeSubscribe(Subscribe subscribe) {
    subscribes.remove(subscribe);
    subscribe.setPlaylist(null);
  }
}
