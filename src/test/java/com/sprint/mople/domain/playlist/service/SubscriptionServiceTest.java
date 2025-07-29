package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.dto.SubscriberCountResponse;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.playlist.repository.SubscriptionRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class SubscriptionServiceTest {

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PlaylistRepository playlistRepository;

  @Mock
  private PlaylistService playlistService;

  @InjectMocks
  private SubscriptionService subscriptionService;

  private User user;
  private Playlist playlist;
  private Subscription subscription;
  private UUID userId;
  private UUID playlistId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    playlistId = UUID.randomUUID();

    user = new User();
    ReflectionTestUtils.setField(user, "id", userId);

    playlist = new Playlist();
    ReflectionTestUtils.setField(playlist, "id", playlistId);

    subscription = new Subscription();
    subscription.setPlaylist(playlist);
  }

  @Test
  @DisplayName("플레이리스트 구독 성공")
  void subscribePlaylist_Success() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.existsByUserAndPlaylist(user, playlist)).thenReturn(false);
    when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

    // when
    Subscription result = subscriptionService.subscribePlaylist(userId, playlistId);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getPlaylist()).isEqualTo(playlist);
    verify(subscriptionRepository).save(any(Subscription.class));
  }

  @Test
  @DisplayName("이미 구독중인 플레이리스트 구독 시도 시 예외 발생")
  void subscribePlaylist_AlreadySubscribed() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.existsByUserAndPlaylist(user, playlist)).thenReturn(true);

    // when & then
    assertThatThrownBy(() -> subscriptionService.subscribePlaylist(userId, playlistId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("이미 구독중인 플레이리스트입니다.");
  }

  @Test
  @DisplayName("구독 취소 성공")
  void unsubscribePlaylist_Success() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.findAllByUserAndPlaylist(
        user,
        playlist
    )).thenReturn(Optional.ofNullable(
        subscription));

    // when
    subscriptionService.unsubscribePlaylist(userId, playlistId);

    // then
    verify(subscriptionRepository).delete(subscription);
    assertThat(subscription.getPlaylist()).isNull();
  }

  @Test
  @DisplayName("존재하지 않는 구독 취소 시도 시 예외 발생")
  void unsubscribePlaylist_NotFound() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.findAllByUserAndPlaylist(
        user,
        playlist
    )).thenReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> subscriptionService.unsubscribePlaylist(userId, playlistId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 사용자의 구독 정보를 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("구독 여부 확인")
  void isSubscribed() {
    // given
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.existsByUserAndPlaylist(user, playlist)).thenReturn(true);

    // when
    boolean result = subscriptionService.isSubscribed(userId, playlistId);

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("사용자의 구독 목록 조회")
  void getUserSubscriptions() {
    // given
    List<Subscription> subscriptions = Arrays.asList(subscription);
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(subscriptionRepository.findAllByUser(user)).thenReturn(subscriptions);

    // when
    List<Subscription> result = subscriptionService.getUserSubscriptions(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result
        .get(0)
        .getPlaylist()).isEqualTo(playlist);
  }

  @Test
  @DisplayName("플레이리스트의 구독자 수 조회")
  void getPlaylistSubscriberCount() {
    // given
    long expectedCount = 5L;
    when(playlistRepository.findById(playlistId)).thenReturn(Optional.of(playlist));
    when(subscriptionRepository.countByPlaylist(playlist)).thenReturn(expectedCount);

    // when
    SubscriberCountResponse result = subscriptionService.getPlaylistSubscriberCount(playlistId);

    // then
    assertThat(result.playlistId()).isEqualTo(playlistId);
    assertThat(result.count()).isEqualTo(expectedCount);
  }

  @Test
  @DisplayName("사용자의 구독 플레이리스트 수 조회")
  void getUserSubscriptionCount() {
    // given
    long expectedCount = 3L;
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(subscriptionRepository.countByUser(user)).thenReturn(expectedCount);

    // when
    long result = subscriptionService.getUserSubscriptionCount(userId);

    // then
    assertThat(result).isEqualTo(expectedCount);
  }
}
