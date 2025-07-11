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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentLikeService {

  private final ContentLikeRepository contentLikeRepository;
  private final ContentRepository contentRepository;
  private final UserRepository userRepository;

  @Transactional
  public void like(UUID userId, UUID contentId) {
    User user = getUser(userId);
    Content content = getContent(contentId);

    if (contentLikeRepository.existsByUserAndContent(user, content)) {
      throw new PlaylistAlreadyLikedException();
    }

    contentLikeRepository.save(new ContentLike(user, content));
  }

  @Transactional
  public void unlike(UUID userId, UUID contentId) {
    User user = getUser(userId);
    Content content = getContent(contentId);

    ContentLike like = contentLikeRepository
        .findByUserAndContent(user, content)
        .orElseThrow(PlaylistNotLikedException::new);

    contentLikeRepository.delete(like);
  }

  public boolean hasLiked(UUID userId, UUID contentId) {
    return contentLikeRepository.existsByUserAndContent(
        getUser(userId),
        getContent(contentId)
    );
  }

  public long countLikes(UUID contentId) {
    if (!contentRepository.existsById(contentId)) {
      throw new IllegalArgumentException();
    }
    return contentLikeRepository.countByContent(getContent(contentId));
  }

  public List<UUID> listUserLikedContentIds(UUID userId) {
    return contentLikeRepository
        .findByUser(getUser(userId))
        .stream()
        .map(cl -> cl
            .getContent()
            .getId())
        .toList();
  }

  private User getUser(UUID userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new EntityNotFoundException("없는 사용자입니다."));
  }

  private Content getContent(UUID contentId) {
    return contentRepository
        .findById(contentId)
        .orElseThrow(() -> new EntityNotFoundException("없는 콘텐츠입니다."));
  }
}
