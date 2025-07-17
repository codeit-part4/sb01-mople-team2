package com.sprint.mople.domain.follow.controller;

import static com.sprint.mople.global.jwt.JwtTokenExtractor.extractUserId;

import com.sprint.mople.domain.follow.dto.FollowCountResponse;
import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.service.FollowService;
import com.sprint.mople.domain.user.dto.UserListResponse;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController implements FollowApi {

  private final FollowService followService;

  private final JwtProvider jwtProvider;

  @PostMapping("/{followeeId}")
  public ResponseEntity<FollowResponse> follow(@PathVariable UUID followeeId,
      HttpServletRequest request) {
    UUID followerId = extractUserId(request, jwtProvider);
    log.debug("팔로우 요청 - 요청한 유저: {}, 팔로우 대상: {}", followeeId, followerId);
    FollowResponse response = followService.follow(followerId, followeeId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{followeeId}")
  public Boolean checkFollowing(@PathVariable UUID followeeId,
      HttpServletRequest request) {
    UUID followerId = extractUserId(request, jwtProvider);
    log.debug("팔로우 상태 조회 요청 - 요청한 유저: {}, 팔로우 대상: {}", followeeId, followerId);
    return followService.checkFollowing(followerId, followeeId);
  }

  @DeleteMapping("/{followeeId}")
  public void unfollow(@PathVariable UUID followeeId, HttpServletRequest request) {
    UUID followerId = extractUserId(request, jwtProvider);
    log.debug("언팔로우 요청 - 요청한 유저: {}, 언팔로우 대상: {}", followeeId, followerId);
    followService.unfollow(followerId, followeeId);
  }

  @GetMapping("/followings/{userId}")
  public ResponseEntity<Page<UserListResponse>> findAllFollowings(@PathVariable UUID userId) {
    log.debug("팔로잉 목록 조회 요청 - 유저: {}", userId);
    Page<UserListResponse> followings = followService.findAllFollowings(userId);
    return ResponseEntity.ok(followings);
  }

  @GetMapping("/followers/{userId}")
  public ResponseEntity<Page<UserListResponse>> findAllFollowers(@PathVariable UUID userId) {
    log.debug("팔로워 목록 조회 요청 - 유저: {}", userId);
    Page<UserListResponse> followers = followService.findAllFollowers(userId);
    return ResponseEntity.ok(followers);
  }

  @GetMapping("/count/{userId}")
  public ResponseEntity<FollowCountResponse> getFollowCount(@PathVariable UUID userId) {
    FollowCountResponse response = followService.getFollowCount(userId);
    return ResponseEntity.ok(response);
  }
}
