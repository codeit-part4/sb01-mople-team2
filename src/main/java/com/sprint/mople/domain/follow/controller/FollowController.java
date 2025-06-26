package com.sprint.mople.domain.follow.controller;

import com.sprint.mople.domain.follow.service.FollowService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

  private final FollowService followService;

  @PostMapping("/{followeeId}")
  public void follow(@PathVariable UUID followeeId,
//      @AuthenticationPrincipal MopleUserDetails userDetails, TODO: MopleUserDetails 구현 필요
      @RequestHeader("USER-ID") UUID followerId) {
    //    UUID followerId = userDetails.getId();
    log.debug("팔로우 요청 - 요청한 유저: {}, 팔로우 대상: {}", followeeId, followerId);
    followService.follow(followerId, followeeId);
  }

  @DeleteMapping("/{followeeId}")
  public void unfollow(@PathVariable UUID followeeId,
//      @AuthenticationPrincipal MopleUserDetails userDetails, TODO: MopleUserDetails 구현 필요
      @RequestHeader("USER-ID") UUID followerId) {
//    UUID followerId = userDetails.getId();
    log.debug("언팔로우 요청 - 요청한 유저: {}, 언팔로우 대상: {}", followeeId, followerId);
    followService.unfollow(followerId, followeeId);
  }
}
