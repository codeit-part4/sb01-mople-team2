package com.sprint.mople.domain.follow.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.service.FollowServiceImpl;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {

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
    ResponseEntity<FollowResponse> response = followController.follow(followeeId,
        httpServletRequest);

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

    // Given
    UUID followerId = UUID.randomUUID();
    UserListResponse userResponse = new UserListResponse("TestUser", "test@email.com", false,
        Instant.now());
    List<UserListResponse> userResponses = List.of(userResponse);
    Page<UserListResponse> userResponsePage = new PageImpl<>(userResponses, PageRequest.of(0, 10),
        userResponses.size());

    when(followService.findAllFollowings(followerId)).thenReturn(userResponsePage);

    // When
    ResponseEntity<Page<UserListResponse>> response = followController.findAllFollowings(followerId);

    // Then
    Page<UserListResponse> body = response.getBody();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(body.getContent().get(0).userName()).isEqualTo("TestUser");
  }

  @Test
  void 팔로워_전체_조회() {
    // Given
    UUID userId = UUID.randomUUID();

    UserListResponse userResponse = new UserListResponse(
        "TestUser",
        "test@email.com",
        false,
        Instant.now()
    );

    List<UserListResponse> userResponses = List.of(userResponse);
    Page<UserListResponse> userResponsePage = new PageImpl<>(
        userResponses,
        PageRequest.of(0, 10),
        userResponses.size()
    );

    when(followService.findAllFollowers(userId)).thenReturn(userResponsePage);

    // When
    ResponseEntity<Page<UserListResponse>> response = followController.findAllFollowers(userId);

    // Then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getContent().get(0).userName()).isEqualTo("TestUser");
  }
}