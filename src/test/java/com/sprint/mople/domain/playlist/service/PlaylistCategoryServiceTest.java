package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.playlist.dto.PlaylistCategoryRequest;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import com.sprint.mople.domain.playlist.entity.PlaylistCategoryMapping;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryMappingRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlaylistCategoryServiceTest {

  @InjectMocks
  private PlaylistCategoryService playlistCategoryService;

  @Mock
  private PlaylistRepository playlistRepository;

  @Mock
  private PlaylistCategoryRepository playlistCategoryRepository;

  @Mock
  private PlaylistCategoryMappingRepository playlistCategoryMappingRepository;


  private User user;
  private Content content;
  private Playlist playlist;
  private PlaylistCategory category;

  private UUID userId;
  private UUID playlistId;
  private UUID contentId;
  private Long categoryId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    playlistId = UUID.randomUUID();
    contentId = UUID.randomUUID();
    categoryId = 1L;

    user = User
        .builder()
        .id(userId)
        .userName("tester")
        .build();

    playlist = Playlist
        .builder()
        .user(user)
        .title("my list")
        .description("desc")
        .isPublic(true)
        .build();
    ReflectionTestUtils.setField(playlist, "id", playlistId);

    content = Content
        .builder()
        .title("movie")
        .build();
    ReflectionTestUtils.setField(content, "id", contentId);

    category = PlaylistCategory
        .builder()
        .name("Pop")
        .build();
    ReflectionTestUtils.setField(category, "id", categoryId);
  }

  @Test
  void 카테고리를_생성할_수_있다() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Jazz");

    PlaylistCategory saved = PlaylistCategory.builder()
        .id(2L)
        .name("Jazz")
        .build();

    when(playlistCategoryRepository.save(any())).thenReturn(saved);

    PlaylistCategory result = playlistCategoryService.createCategory(request);

    assertThat(result.getName()).isEqualTo("Jazz");
    verify(playlistCategoryRepository).save(any());
  }

  @Test
  void 카테고리를_수정할_수_있다() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Rock");

    when(playlistCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

    PlaylistCategory updated = playlistCategoryService.updateCategory(categoryId, request);

    assertThat(updated.getName()).isEqualTo("Rock");
    verify(playlistCategoryRepository).findById(categoryId);
  }

  @Test
  void 존재하지_않는_카테고리_수정시_예외발생() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Hip-hop");

    when(playlistCategoryRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () ->
        playlistCategoryService.updateCategory(999L, request)
    );
  }

  @Test
  void 카테고리를_삭제할_수_있다() {
    when(playlistCategoryRepository.existsById(categoryId)).thenReturn(true);

    playlistCategoryService.deleteCategory(categoryId);

    verify(playlistCategoryRepository).deleteById(categoryId);
  }

  @Test
  void 존재하지_않는_카테고리_삭제시_예외발생() {
    when(playlistCategoryRepository.existsById(anyLong())).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () ->
        playlistCategoryService.deleteCategory(123L)
    );
  }

  @Test
  void 카테고리_전체_조회() {
    List<PlaylistCategory> categories = List.of(category);
    when(playlistCategoryRepository.findAll()).thenReturn(categories);

    List<PlaylistCategory> result = playlistCategoryService.findAllCategories();

    assertThat(result).hasSize(1);
  }

  @Test
  void 플레이리스트에_카테고리를_추가할_수_있다() {
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(playlistCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(playlistCategoryMappingRepository.existsByPlaylistAndCategory(playlist, category)).thenReturn(false);

    playlistCategoryService.addCategoryToPlaylist(playlistId, categoryId);

    verify(playlistCategoryMappingRepository).save(any(PlaylistCategoryMapping.class));
  }

  @Test
  void 플레이리스트에_이미_있는_카테고리를_중복_추가하지_않는다() {
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(playlistCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(playlistCategoryMappingRepository.existsByPlaylistAndCategory(playlist, category)).thenReturn(true);

    playlistCategoryService.addCategoryToPlaylist(playlistId, categoryId);

    verify(playlistCategoryMappingRepository, never()).save(any());
  }

  @Test
  void 플레이리스트에서_카테고리를_제거할_수_있다() {
    PlaylistCategoryMapping mapping = PlaylistCategoryMapping.builder()
        .playlist(playlist)
        .category(category)
        .build();

    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(playlistCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
    when(playlistCategoryMappingRepository.findByPlaylistAndCategory(playlist, category)).thenReturn(Optional.of(mapping));

    playlistCategoryService.removeCategoryFromPlaylist(playlistId, categoryId);

    verify(playlistCategoryMappingRepository).delete(mapping);
  }

  @Test
  void 존재하지_않는_플레이리스트에_카테고리_추가시_예외발생() {
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () ->
        playlistCategoryService.addCategoryToPlaylist(playlistId, categoryId)
    );
  }
}
