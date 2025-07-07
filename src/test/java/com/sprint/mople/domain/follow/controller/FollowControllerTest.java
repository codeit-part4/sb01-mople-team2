package com.sprint.mople.domain.follow.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.repository.FollowRepository;
import com.sprint.mople.domain.follow.service.FollowServiceImpl;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {

  @Mock
  FollowRepository followRepository;

  @Mock
  FollowServiceImpl followService;

  @Mock
  JwtProvider jwtProvider;

  @Mock
  HttpServletRequest httpServletRequest;

  @InjectMocks
  FollowController followController;

  @Test
  void 팔로우_성공() {
    // Given
    UUID followerId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();
    FollowResponse followResponse = new FollowResponse(followerId, followeeId);
    when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtProvider.getUserId("token")).thenReturn(followerId);
    when(followService.follow(followerId, followeeId))
        .thenReturn(followResponse);

    // When
    ResponseEntity<FollowResponse> response = followController.follow(followeeId, httpServletRequest);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().followeeId()).isEqualTo(followeeId);
  }

  @Test
  void 언팔로우_성공() {
    // Given
    UUID followerId = UUID.randomUUID();
    UUID followeeId = UUID.randomUUID();
    when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
    when(jwtProvider.getUserId("token")).thenReturn(followerId);

    // When
    followController.unfollow(followeeId, httpServletRequest);

    // Then
    verify(followService).unfollow(followerId, followeeId);

  }
  @Test
  void 팔로잉_전체_조회() {
  }

  @Test
  void 팔로워_전체_조회() {
  }
}