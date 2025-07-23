package com.sprint.mople.domain.content.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.ContentLike;
import com.sprint.mople.domain.content.repository.ContentLikeRepository;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.playlist.exception.PlaylistAlreadyLikedException;
import com.sprint.mople.domain.playlist.exception.PlaylistNotLikedException;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentLikeServiceTest {

  private final UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
  private final UUID contentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1");

  @Mock
  private ContentLikeRepository contentLikeRepository;

  @Mock
  private ContentRepository contentRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private ContentLikeService contentLikeService;

  private User user;
  private Content content;

  @BeforeEach
  void 설정() {

    user = User
        .builder()
        .id(userId)
        .build();
    content = Content
        .builder()
        .build();
    ReflectionTestUtils.setField(content, "id", contentId);


  }

  @Test
  void 좋아요를_저장한다() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentLikeRepository.existsByUserAndContent(user, content)).thenReturn(false);


    contentLikeService.like(userId, contentId);

    verify(contentLikeRepository).save(any(ContentLike.class));
  }

  @Test
  void 이미_좋아요한_경우_예외가_발생한다() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentLikeRepository.existsByUserAndContent(user, content)).thenReturn(true);

    assertThatThrownBy(() -> contentLikeService.like(userId, contentId))
        .isInstanceOf(PlaylistAlreadyLikedException.class);
  }

  @Test
  void 좋아요_취소시_정상적으로_삭제된다() {
    ContentLike like = new ContentLike(user, content);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentLikeRepository.findByUserAndContent(user, content)).thenReturn(Optional.of(like));

    contentLikeService.unlike(userId, contentId);

    verify(contentLikeRepository).delete(like);
  }

  @Test
  void 좋아요하지_않은_경우_취소하면_예외가_발생한다() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentLikeRepository.findByUserAndContent(user, content)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> contentLikeService.unlike(userId, contentId))
        .isInstanceOf(PlaylistNotLikedException.class);
  }

  @Test
  void 좋아요한_경우_true를_반환한다() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentLikeRepository.existsByUserAndContent(user, content)).thenReturn(true);

    boolean liked = contentLikeService.hasLiked(userId, contentId);

    assertThat(liked).isTrue();
  }

  @Test
  void 좋아요_수를_정상적으로_반환한다() {
    when(contentRepository.findById(contentId)).thenReturn(Optional.of(content));
    when(contentRepository.existsById(contentId)).thenReturn(true);
    when(contentLikeRepository.countByContent(content)).thenReturn(42L);

    long count = contentLikeService.countLikes(contentId);

    assertThat(count).isEqualTo(42);
  }

  @Test
  void 콘텐츠가_없는_경우_좋아요_수_조회시_예외가_발생한다() {
    when(contentRepository.existsById(contentId)).thenReturn(false);

    assertThatThrownBy(() -> contentLikeService.countLikes(contentId))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void 사용자가_좋아요한_콘텐츠_ID_목록을_반환한다() {
    ContentLike like1 = new ContentLike(user, content);
    Content otherContent = Content
        .builder()
        .build();
    ContentLike like2 = new ContentLike(user, otherContent);

    ReflectionTestUtils.setField(like1, "id", 1L);
    ReflectionTestUtils.setField(like2, "id", 2L);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentLikeRepository.findByUser(user)).thenReturn(List.of(like1, like2));

    List<UUID> result = contentLikeService.listUserLikedContentIds(userId);

    assertThat(result).containsExactlyInAnyOrder(content.getId(), otherContent.getId());
  }

  @Test
  void 사용자가_존재하지_않으면_예외가_발생한다() {
    UUID unknownId = UUID.randomUUID();
    when(userRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> contentLikeService.hasLiked(unknownId, contentId))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void 콘텐츠가_존재하지_않으면_예외가_발생한다() {
    UUID unknownId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(contentRepository.findById(unknownId)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> contentLikeService.hasLiked(userId, unknownId))
        .isInstanceOf(EntityNotFoundException.class);
  }
}
