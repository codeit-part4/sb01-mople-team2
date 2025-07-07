package com.sprint.mople.domain.playlist.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Playlist Like", description = "플레이리스트 좋아요 API")
public interface PlaylistLikeApi {

  @Operation(summary = "플레이리스트 좋아요", description = "해당 플레이리스트에 좋아요를 누릅니다.")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "200", description = "좋아요 성공"),
          @ApiResponse(responseCode = "400", description = "이미 좋아요한 경우 등 에러")
      }
  )
  @PostMapping
  ResponseEntity<Void> like(
      @Parameter(description = "플레이리스트 ID", required = true)
      @PathVariable UUID playlistId
  );

  @Operation(summary = "플레이리스트 좋아요 취소", description = "해당 플레이리스트에 좋아요를 취소합니다.")
  @ApiResponses(
      {
          @ApiResponse(responseCode = "204", description = "좋아요 취소 성공"),
          @ApiResponse(responseCode = "400", description = "좋아요를 누르지 않은 경우 등 에러")
      }
  )
  @DeleteMapping
  ResponseEntity<Void> unlike(
      @Parameter(description = "플레이리스트 ID", required = true)
      @PathVariable UUID playlistId
  );

  @Operation(summary = "내가 좋아요 눌렀는지 확인", description = "현재 사용자가 해당 플레이리스트에 좋아요를 눌렀는지 여부를 확인합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping("/me")
  ResponseEntity<Boolean> isLiked(
      @Parameter(description = "플레이리스트 ID", required = true)
      @PathVariable UUID playlistId
  );

  @Operation(summary = "플레이리스트 좋아요 수 조회", description = "해당 플레이리스트의 총 좋아요 수를 조회합니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  @GetMapping
  ResponseEntity<Long> likeCount(
      @Parameter(description = "플레이리스트 ID", required = true)
      @PathVariable UUID playlistId
  );
}

