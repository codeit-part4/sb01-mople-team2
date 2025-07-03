package com.sprint.mople.domain.follow.service;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.follow.exception.FollowAlreadyExistsException;
import com.sprint.mople.domain.follow.exception.FollowNotFoundException;
import com.sprint.mople.domain.follow.mapper.FollowMapper;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int DEFAULT_PAGE_NUMBER = 0;

  private final FollowRepository followRepository;
  private final UserRepository userRepository;
  private final FollowMapper followMapper;
  private final NotificationService notificationService;

  @Transactional
  @Override
  public FollowResponse follow(UUID followerId, UUID followeeId) {
    log.debug("팔로우 생성 시작 - 유저: {}, 팔로우 대상: {}", followerId, followeeId);
    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 - id: " + followerId));
    User followee = userRepository.findById(followeeId)
        .orElseThrow(
            () -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다 - id: " + followeeId));

    if (followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId).isPresent()){
      throw new FollowAlreadyExistsException();
    }
    Follow follow = new Follow(follower, followee);
    followRepository.save(follow);
    log.debug("팔로우 생성 완료 - 유저: {}, 팔로우 대상: {}", followerId, followeeId);

    String content = String.format("%s님이 당신을 팔로우하기 시작했습니다.", follower.getUserName());
    notificationService.send(follower, NotificationType.NEW_FOLLOWER, content, null);
    log.debug("팔로우 알림 전송 완료 - 대상: {}", followerId);

    return followMapper.toDto(follow);
  }

  @Transactional
  @Override
  public void unfollow(UUID followerId, UUID followeeId) {
    log.debug("언팔로우 시작 - 유저: {}, 언팔로우 대상: {}", followerId, followeeId);
    Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
        .orElseThrow(FollowNotFoundException::new);
    followRepository.delete(follow);
    log.debug("언팔로우 완료 - 유저: {}, 언팔로우 대상: {}", followerId, followeeId);
  }

  @Override
  public Page<UserListResponse> findAllFollowings(UUID userId) {
    int page = DEFAULT_PAGE_NUMBER;
    int size = DEFAULT_PAGE_SIZE;
    log.debug("팔로잉 목록 조회 시작 - 유저: {}, 페이지: {}, 사이즈: {}", userId, page, size);
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 - id: " + userId));

    Page<User> followees = followRepository.findFolloweesByFollower(user, pageable);
    if (followees.isEmpty()) {
      log.info("팔로잉 목록이 비어 있습니다 - 유저: {}", userId);
      return Page.empty(pageable);
    }

    log.debug("팔로잉 목록 조회 완료 - 유저: {}, 페이지: {}, 사이즈: {}", userId, page, size);
    return followees.map(followee -> new UserListResponse(
        followee.getUserName(),
        followee.getEmail(),
        followee.getIsLocked(),
        followee.getCreateAt()
    ));
  }

  @Override
  public Page<UserListResponse> findAllFollowers(UUID userId) {
    int page = DEFAULT_PAGE_NUMBER;
    int size = DEFAULT_PAGE_SIZE;
    log.debug("팔로워 목록 조회 시작 - 유저: {}, 페이지: {}, 사이즈: {}", userId, page, size);
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 - id: " + userId));

    Page<User> followers = followRepository.findFollowersByFollowee(user, pageable);
    if (followers.isEmpty()) {
      log.info("팔로워 목록이 비어 있습니다 - 유저: {}", userId);
      return Page.empty(pageable);
    }

    log.debug("팔로워 목록 조회 완료 - 유저: {}, 페이지: {}, 사이즈: {}", userId, page, size);
    return followers.map(follower -> new UserListResponse(
        follower.getUserName(),
        follower.getEmail(),
        follower.getIsLocked(),
        follower.getCreateAt()
    ));
  }
}
