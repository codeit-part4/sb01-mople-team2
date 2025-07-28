package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.dto.PlaylistCategoryRequest;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import com.sprint.mople.domain.playlist.entity.PlaylistCategoryMapping;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryMappingRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlaylistCategoryService {

  private final PlaylistRepository playlistRepository;
  private final PlaylistCategoryRepository playlistCategoryRepository;
  private final PlaylistCategoryMappingRepository playlistCategoryMappingRepository;

  public PlaylistCategory createCategory(PlaylistCategoryRequest dto) {
    PlaylistCategory category = PlaylistCategory
        .builder()
        .name(dto.name())
        .build();

    return playlistCategoryRepository.save(category);
  }

  public PlaylistCategory updateCategory(Long categoryId, PlaylistCategoryRequest dto) {
    PlaylistCategory category = playlistCategoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

    category.setName(dto.name());
    return category;
  }

  public void deleteCategory(Long categoryId) {
    if (!playlistCategoryRepository.existsById(categoryId)) {
      throw new IllegalArgumentException("카테고리를 찾을 수 없습니다.");
    }
    playlistCategoryRepository.deleteById(categoryId);
  }

  @Transactional(readOnly = true)
  public List<PlaylistCategory> findAllCategories() {
    return playlistCategoryRepository.findAll();
  }

  public void addCategoryToPlaylist(UUID playlistId, Long categoryId) {
    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(() -> new IllegalArgumentException("플레이리스트를 찾을 수 없습니다."));

    PlaylistCategory category = playlistCategoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

    // 이미 연결된 경우 중복 저장 방지
    boolean exists = playlistCategoryMappingRepository.existsByPlaylistAndCategory(
        playlist,
        category
    );
    if (exists) {
      return;
    }

    PlaylistCategoryMapping mapping = PlaylistCategoryMapping
        .builder()
        .playlist(playlist)
        .category(category)
        .build();

    playlist.addCategoryMapping(mapping);
    category.addPlaylistMapping(mapping);

    playlistCategoryMappingRepository.save(mapping);
  }

  public void removeCategoryFromPlaylist(UUID playlistId, Long categoryId) {
    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(() -> new IllegalArgumentException("플레이리스트를 찾을 수 없습니다."));

    PlaylistCategory category = playlistCategoryRepository
        .findById(categoryId)
        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

    Optional<PlaylistCategoryMapping> mappingOpt =
        playlistCategoryMappingRepository.findByPlaylistAndCategory(playlist, category);

    mappingOpt.ifPresent(mapping -> {
      playlist.removeCategoryMapping(mapping);
      category.removePlaylistMapping(mapping);
      playlistCategoryMappingRepository.delete(mapping);
    });
  }
}


