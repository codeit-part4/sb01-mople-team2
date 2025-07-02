package com.sprint.mople.domain.follow.controller;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.follow.dto.FollowResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Follow", description = "팔로우 관련 API")
public interface FollowApi {

  @Operation(summary = "팔로우", description = "특정 사용자를 팔로우합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "사용자를 성공적으로 팔로우함",
          content = @Content(schema = @Schema(implementation = FollowResponse.class))
      )
  })
  ResponseEntity<FollowResponse> follow(@PathVariable UUID followeeId, HttpServletRequest request);

  @Operation(summary = "팔로우 취소", description = "특정 사용자를 언팔로우합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204", description = "팔로우가 성공적으로 취소됨",
          content = @Content(schema = @Schema(implementation = ChatRoomResponse.class))
      )
  })
  void unfollow(@PathVariable UUID followeeId,HttpServletRequest request);
}
