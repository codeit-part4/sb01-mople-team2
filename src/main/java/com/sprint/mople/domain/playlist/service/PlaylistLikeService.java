package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistLike;
import com.sprint.mople.domain.playlist.exception.AlreadyLikedException;
import com.sprint.mople.domain.playlist.exception.NotLikedException;
import com.sprint.mople.domain.playlist.exception.PlaylistNotFoundException;
import com.sprint.mople.domain.playlist.repository.PlaylistLikeRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.UserNotFoundException;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional

public class PlaylistLikeService {

  private final PlaylistRepository playlistRepository;
  private final PlaylistLikeRepository playlistLikeRepository;
  private final UserRepository userRepository;

  public void likePlaylist(UUID userId, UUID playlistId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(UserNotFoundException::new);

    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);

    if (playlistLikeRepository.existsByUserAndPlaylist(user, playlist)) {
      throw new AlreadyLikedException();
    }

    playlistLikeRepository.save(new PlaylistLike(user, playlist));
  }

  public void unlikePlaylist(UUID userId, UUID playlistId) {
    User user = userRepository
        .findById(userId)
        .orElseThrow(UserNotFoundException::new);

    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);

    PlaylistLike like = playlistLikeRepository
        .findByUserAndPlaylist(user, playlist)
        .orElseThrow(NotLikedException::new);

    playlistLikeRepository.delete(like);
  }

  @Transactional(readOnly = true)
  public boolean isLiked(UUID userId, UUID playlistId) {
    return userRepository
        .findById(userId)
        .flatMap(u -> playlistRepository
            .findById(playlistId)
            .map(p -> playlistLikeRepository.existsByUserAndPlaylist(u, p)))
        .orElse(false);
  }

  @Transactional(readOnly = true)
  public long getLikeCount(UUID playlistId) {
    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);
    return playlistLikeRepository.countByPlaylist(playlist);
  }
}

