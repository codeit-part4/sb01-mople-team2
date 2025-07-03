package com.sprint.mople.domain.playlist.entity;

import com.sprint.mople.domain.content.entity.Content;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "playlists_contents")
@Getter
@NoArgsConstructor
public class PlaylistContent {

  @Id
  @GeneratedValue
  @Column(name = "playlist_content_id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "playlist_id", nullable = false)
  private Playlist playlist;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "content_id", nullable = false)
  private Content content;

  public void setPlaylist(Playlist playlist) {
    if (this.playlist != null) {
      this.playlist.getPlaylistContents().remove(this);
    }
    this.playlist = playlist;
    if (playlist != null) {
      playlist.getPlaylistContents().add(this);
    }
  }

  public void setContent(Content content) {
    if (this.content != null) {
      this.content.getPlaylistContents().remove(this);
    }
    this.content = content;
    if (content != null) {
      content.getPlaylistContents().add(this);
    }
  }
}

