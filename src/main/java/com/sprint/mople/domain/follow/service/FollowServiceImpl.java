package com.sprint.mople.domain.follow.service;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.follow.mapper.FollowMapper;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final FollowRepository followRepository;
  private final UserRepository userRepository;
  private final FollowMapper followMapper;

  @Transactional
  @Override
  public FollowResponse follow(UUID followerId, UUID followeeId) {
    log.debug("팔로우 생성 시작 - 유저: {}, 팔로우 대상: {}", followerId, followeeId);
    User follower = userRepository.findById(followerId)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 - id: " + followerId));
    User followee = userRepository.findById(followeeId)
        .orElseThrow(
            () -> new IllegalArgumentException("팔로우 대상 유저를 찾을 수 없습니다 - id: " + followeeId));
    Follow follow = new Follow(follower, followee);
    followRepository.save(follow);
    log.debug("팔로우 생성 완료 - 유저: {}, 팔로우 대상: {}", followerId, followeeId);
    return followMapper.toDto(follow);
  }

  @Transactional
  @Override
  public void unfollow(UUID followerId, UUID followeeId) {
    log.debug("언팔로우 시작 - 유저: {}, 언팔로우 대상: {}", followerId, followeeId);
    Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
        .orElseThrow(() -> new IllegalArgumentException(
            "팔로우 관계를 찾을 수 없습니다 - followerId: " + followerId + " followeeId: " + followeeId));
    followRepository.delete(follow);
    log.debug("언팔로우 완료 - 유저: {}, 언팔로우 대상: {}", followerId, followeeId);
  }
}
