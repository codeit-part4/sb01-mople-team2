package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistContent;
import com.sprint.mople.domain.playlist.exception.DuplicatePlaylistContentException;
import com.sprint.mople.domain.playlist.exception.PlaylistContentNotFoundException;
import com.sprint.mople.domain.playlist.exception.PlaylistIllegalAccessException;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaylistServiceImplTest {

  @Mock private PlaylistRepository playlistRepository;
  @Mock private UserRepository userRepository;
  @Mock private ContentRepository contentRepository;
  @InjectMocks private PlaylistServiceImpl playlistService;

  private UUID userId;
  private UUID playlistId;
  private UUID contentId;
  private User user;
  private Playlist playlist;
  private Content content;

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
  }


  @Test
  void 플레이리스트_생성에_성공() {
    PlaylistCreateRequest req = new PlaylistCreateRequest("t", "d", true);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> {
      Playlist p = inv.getArgument(0);
      ReflectionTestUtils.setField(p, "id", playlistId); // id 주입
      return p;
    });

    PlaylistResponse res = playlistService.createPlaylist(req, userId);

    assertThat(res.id()).isEqualTo(playlistId);
    assertThat(res.title()).isEqualTo("t");
    verify(playlistRepository).save(any(Playlist.class));
  }

  @Test
  void 플레이리스트_수정에_성공() {
    PlaylistUpdateRequest req = new PlaylistUpdateRequest("new", "newDesc", false);

    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

    PlaylistResponse res = playlistService.updatePlaylist(playlistId, req, userId);

    assertThat(res.title()).isEqualTo("new");
    assertThat(res.isPublic()).isFalse();
  }

  @Test
  void 플레이리스트_삭제에_성공() {
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

    playlistService.deletePlaylist(playlistId, userId);

    verify(playlistRepository).delete(playlist);
  }


  @Test
  void 플레이리스트_컨텐츠_등록에_성공() {
    PlaylistContentRequest req = new PlaylistContentRequest(contentId);

    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));

    playlistService.addContent(playlistId, req, userId);

    assertThat(playlist.getPlaylistContents()).hasSize(1);
  }

  @Test
  void 플레이리스트에_중복된_컨텐츠를_등록하면_예외_발생() {
    // 이미 추가
    PlaylistContent link = new PlaylistContent();
    link.setPlaylist(playlist);
    link.setContent(content);
    playlist
        .getPlaylistContents()
        .add(link);

    PlaylistContentRequest req = new PlaylistContentRequest(contentId);

    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));

    assertThrows(
        DuplicatePlaylistContentException.class,
        () -> playlistService.addContent(playlistId, req, userId)
    );
  }

  @Test
  void 플레이리스트_컨텐츠_삭제에_성공() {
    PlaylistContent playlistContent = new PlaylistContent();
    playlistContent.setPlaylist(playlist);
    playlistContent.setContent(content);

    PlaylistContentRequest req = new PlaylistContentRequest(contentId);
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));

    playlistService.removeContent(playlistId, req, userId);

    assertThat(playlist.getPlaylistContents()).isEmpty();
  }

  @Test
  void 플레이리스트에서_존재하지_않는_컨텐츠를_삭제하면_예외처리() {
    PlaylistContentRequest req = new PlaylistContentRequest(contentId);
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));

    assertThrows(
        PlaylistContentNotFoundException.class,
        () -> playlistService.removeContent(playlistId, req, userId)
    );
  }

  @Test
  void 공개된_플레이리스트_조회에_성공() {
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

    PlaylistResponse res = playlistService.getPlaylistById(playlistId, UUID.randomUUID());

    assertThat(res.id()).isEqualTo(playlistId);
  }

  @Test
  void 비공개된_플레이리스트에_권한이_없는_유저가_접근시_예외() {
    playlist.setIsPublic(false);
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));

    assertThrows(
        PlaylistIllegalAccessException.class,
        () -> playlistService.getPlaylistById(playlistId, UUID.randomUUID())
    );
  }
}
