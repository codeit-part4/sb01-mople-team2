package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.dto.SubscriberCountResponse;
import com.sprint.mople.domain.playlist.dto.SubscriptionResponse;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.playlist.repository.SubscriptionRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.NotFoundException;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final PlaylistService playlistService;
  private final UserRepository userRepository;
  private final PlaylistRepository playlistRepository;

  @Transactional
  public Subscription subscribePlaylist(UUID userId, UUID playlistId) {
    // 이미 구독중인지 확인
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
    Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();

    if (subscriptionRepository.existsByUserAndPlaylist(user, playlist)) {
      throw new IllegalArgumentException("이미 구독중인 플레이리스트입니다.");
    }

    Subscription subscription = new Subscription();
    subscription.setPlaylist(playlist);
    subscription.setUser(user);


    return subscriptionRepository.save(subscription);
  }

  @Transactional
  public void unsubscribePlaylist(Long subscribeId) {
    Subscription subscription = subscriptionRepository
        .findById(subscribeId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구독입니다."));

    subscription.setPlaylist(null); // 연관관계 제거
    subscriptionRepository.delete(subscription);
  }

  public boolean isSubscribed(UUID userId, UUID playlistId) {
    User user = userRepository.findById(userId).orElseThrow();
    Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
    return subscriptionRepository.existsByUserAndPlaylist(user, playlist);
  }

  public List<SubscriptionResponse> getUserSubscriptions(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return subscriptionRepository.findAllByUser(user)
        .stream()
        .map(SubscriptionResponse::from)
        .toList();
  }

  public List<Subscription> getPlaylistSubscribers(UUID playlistId) {
    Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
    return subscriptionRepository.findAllByPlaylist(playlist);
  }

  public SubscriberCountResponse getPlaylistSubscriberCount(UUID playlistId) {
    Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
    Long count = subscriptionRepository.countByPlaylist(playlist);
    return new SubscriberCountResponse(playlistId, count);
  }

  public long getUserSubscriptionCount(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return subscriptionRepository.countByUser(user);
  }
}
