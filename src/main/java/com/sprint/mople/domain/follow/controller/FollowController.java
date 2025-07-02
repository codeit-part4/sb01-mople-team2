package com.sprint.mople.domain.follow.controller;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.service.FollowService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
  public ResponseEntity<FollowResponse> follow(@PathVariable UUID followeeId, HttpServletRequest request) {
    String token = JwtTokenExtractor.resolveToken(request);
    UUID followerId = jwtProvider.getUserId(token);
    log.debug("팔로우 요청 - 요청한 유저: {}, 팔로우 대상: {}", followeeId, followerId);
    FollowResponse response = followService.follow(followerId, followeeId);
    return ResponseEntity.ok(response);
  }


  @DeleteMapping("/{followeeId}")
  public void unfollow(@PathVariable UUID followeeId,HttpServletRequest request) {
    String token = JwtTokenExtractor.resolveToken(request);
    UUID followerId = jwtProvider.getUserId(token);
    log.debug("언팔로우 요청 - 요청한 유저: {}, 언팔로우 대상: {}", followeeId, followerId);
    followService.unfollow(followerId, followeeId);
  }
}
