package com.sprint.mople.follow;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.follow.mapper.FollowMapper;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.follow.service.FollowServiceImpl;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FollowTest {

  @Mock
  FollowRepository followRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  FollowMapper followMapper;

  @InjectMocks
  FollowServiceImpl followService;

  @Test
  void 팔로우_성공() {
    // Given
    UUID followerId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();

    // When
    when(userRepository.findById(followerId)).thenReturn(Optional.of(new User()));
    when(userRepository.findById(followeeId)).thenReturn(Optional.of(new User()));
    when(followMapper.toDto(any(Follow.class))).thenReturn(
        new FollowResponse(followerId, followeeId));
    FollowResponse response = followService.follow(followerId, followeeId);

    // Then
    assertNotNull(response);
    assert (followerId).equals(response.followerId());
    assert (followeeId).equals(response.followeeId());
  }

  @Test
  void 언팔로우_성공() {
    // Given
    UUID followerId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();
    Follow follow = new Follow(new User(), new User());

    // When
    when(followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId))
        .thenReturn(Optional.of(follow));
    followService.unfollow(followerId, followeeId);

    // Then
  }
}
