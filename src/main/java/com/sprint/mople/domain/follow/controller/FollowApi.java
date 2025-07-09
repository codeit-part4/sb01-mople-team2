package com.sprint.mople.domain.follow.controller;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.user.dto.UserListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowApi {

  @Operation(summary = "팔로우", description = "특정 사용자를 팔로우합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "사용자를 성공적으로 팔로우함"
      )
  })
  ResponseEntity<FollowResponse> follow(@PathVariable UUID followeeId, HttpServletRequest request);

  @Operation(summary = "팔로우 취소", description = "특정 사용자를 언팔로우합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "팔로우가 성공적으로 취소됨"
      )
  })
  void unfollow(@PathVariable UUID followeeId, HttpServletRequest request);

  @Operation(summary = "팔로잉 목록", description = "사용자가 팔로우하는 대상 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "팔로잉 목록 조회 성공"
      )
  })
  ResponseEntity<Page<UserListResponse>> findAllFollowings(HttpServletRequest request);

  @Operation(summary = "팔로워 목록", description = "사용자를 팔로우하는 사람들의 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "팔로워 목록 조회 성공"
      )
  })
  ResponseEntity<Page<UserListResponse>> findAllFollowers(HttpServletRequest request);
}
