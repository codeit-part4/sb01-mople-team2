package com.sprint.mople.follow;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.follow.mapper.FollowMapper;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.follow.service.FollowService;
import com.sprint.mople.domain.user.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FollowTest {
  @InjectMocks
  FollowService followService;

  @Mock
  FollowRepository followRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  FollowMapper followMapper;

  @Test
  void 팔로우_성공(){
    // Given
    UUID followerId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();

    // When
    when(userRepository.findById(followerId)).thenReturn(Optional.of(new User(followerId)));
    when(userRepository.findById(followeeId)).thenReturn(Optional.of(new User(followeeId)));
    when(followMapper.toDto(any(Follow.class))).thenReturn(new FollowResponse(followerId, followeeId));
    FollowResponse response = followService.follow(followerId, followeeId);

    // Then
    assertNotNull(response);
    assert(followerId).equals(response.followerId());
    assert(followeeId).equals(response.followeeId());
  }

}
