package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.playlist.dto.PlaylistCategoryRequest;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryMappingRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class PlaylistCategoryServiceTest {

  @Autowired
  private PlaylistCategoryService playlistCategoryService;

  @Autowired
  private PlaylistRepository playlistRepository;

  @Autowired
  private PlaylistCategoryRepository playlistCategoryRepository;

  @Autowired
  private PlaylistCategoryMappingRepository playlistCategoryMappingRepository;

  private User user;
  private Content content;
  private Playlist savedPlaylist;
  private PlaylistCategory savedCategory;

  private UUID userId;
  private UUID playlistId;
  private UUID contentId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    playlistId = UUID.randomUUID();
    contentId = UUID.randomUUID();

    user = User
        .builder()
        .id(userId)
        .userName("tester")
        .build();

    savedPlaylist = Playlist
        .builder()
        .user(user)
        .title("my list")
        .description("desc")
        .isPublic(true)
        .build();
    ReflectionTestUtils.setField(savedPlaylist, "id", playlistId);

    content = Content
        .builder()
        .title("movie")
        .build();
    ReflectionTestUtils.setField(content, "id", contentId);

    savedCategory = PlaylistCategory
        .builder()
        .name("Pop")
        .build();
    playlistCategoryRepository.save(savedCategory);
  }

  @Test
  void 카테고리를_생성할_수_있다() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Jazz");

    PlaylistCategory created = playlistCategoryService.createCategory(request);

    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo("Jazz");
  }

  @Test
  void 카테고리를_수정할_수_있다() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Rock");

    PlaylistCategory updated = playlistCategoryService.updateCategory(
        savedCategory.getId(),
        request
    );

    assertThat(updated.getName()).isEqualTo("Rock");
  }

  @Test
  void 카테고리를_삭제할_수_있다() {
    Long id = savedCategory.getId();

    playlistCategoryService.deleteCategory(id);

    assertThat(playlistCategoryRepository.findById(id)).isEmpty();
  }

  @Test
  void 카테고리를_모두_조회할_수_있다() {
    List<PlaylistCategory> all = playlistCategoryService.findAllCategories();

    assertThat(all).isNotEmpty();
  }

  @Test
  void 플레이리스트에_카테고리를_추가할_수_있다() {

    playlistCategoryService.addCategoryToPlaylist(savedPlaylist.getId(), savedCategory.getId());

    assertThat(
        playlistCategoryMappingRepository.existsByPlaylistAndCategory(savedPlaylist, savedCategory)
    ).isTrue();
  }

  @Test
  void 플레이리스트에서_카테고리를_제거할_수_있다() {
    playlistCategoryService.addCategoryToPlaylist(savedPlaylist.getId(), savedCategory.getId());
    playlistCategoryService.removeCategoryFromPlaylist(
        savedPlaylist.getId(),
        savedCategory.getId()
    );

    assertThat(
        playlistCategoryMappingRepository.existsByPlaylistAndCategory(savedPlaylist, savedCategory)
    ).isFalse();
  }

  @Test
  void 존재하지_않는_카테고리_수정시_예외발생() {
    PlaylistCategoryRequest request = new PlaylistCategoryRequest("Hip-hop");

    assertThrows(
        IllegalArgumentException.class, () ->
            playlistCategoryService.updateCategory(999L, request)
    );
  }

  @Test
  void 존재하지_않는_플레이리스트_카테고리_추가시_예외발생() {
    UUID fakeId = UUID.randomUUID();

    assertThrows(
        IllegalArgumentException.class, () ->
            playlistCategoryService.addCategoryToPlaylist(fakeId, savedCategory.getId())
    );
  }
}
