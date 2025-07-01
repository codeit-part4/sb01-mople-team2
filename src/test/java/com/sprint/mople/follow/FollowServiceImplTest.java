package com.sprint.mople.follow;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import com.sprint.mople.domain.follow.mapper.FollowMapper;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.follow.service.FollowServiceImpl;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FollowServiceImplTest {

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
    when(userRepository.findById(followerId)).thenReturn(Optional.of(new User()));
    when(userRepository.findById(followeeId)).thenReturn(Optional.of(new User()));
    when(followMapper.toDto(any(Follow.class))).thenReturn(
        new FollowResponse(followerId, followeeId));

    // When
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

  @Test
  void 팔로잉_페이징_조회_성공(){
    // Given
    UUID userId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();
    int page = 0;
    int size = 10;

    UserListResponse response = new UserListResponse(
        "username", "user@email.com", false, Instant.now());
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    when(followRepository.findByFollowerId(userId, pageable))
        .thenReturn(List.of(followeeId));

    // When
    Page<UserListResponse> responses = followService.findAllFollowings(userId, page, size);

    // Then
    assertNotNull(followService.findAllFollowings(userId, page, size));
  }
}
