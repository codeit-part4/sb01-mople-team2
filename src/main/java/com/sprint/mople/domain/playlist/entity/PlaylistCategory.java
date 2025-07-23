package com.sprint.mople.domain.playlist.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PlaylistCategory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaylistCategoryMapping> playlists = new ArrayList<>();

  public void addPlaylistMapping(PlaylistCategoryMapping mapping) {
    this.playlists.add(mapping);
    mapping.setCategory(this);
  }

  public void removePlaylistMapping(PlaylistCategoryMapping mapping) {
    this.playlists.remove(mapping);
    mapping.setCategory(null);
  }
}
