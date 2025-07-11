package com.sprint.mople.domain.playlist.api;

import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Playlist", description = "플레이리스트 API")
@RequestMapping("/api/playlists")
public interface PlaylistApi {

  @Operation(
      summary = "플레이리스트 생성",
      description = "새로운 플레이리스트를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "생성된 플레이리스트 정보 반환")
      }
  )
  @PostMapping("/")
  ResponseEntity<PlaylistResponse> createPlaylist(

      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "플레이리스트 생성 요청 DTO",
          required = true,
          content = @Content(schema = @Schema(implementation = PlaylistCreateRequest.class))
      )
      @RequestBody @Valid PlaylistCreateRequest request
  );

  @Operation(
      summary = "플레이리스트 수정",
      description = "플레이리스트 제목, 설명, 공개 여부를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "수정된 플레이리스트 정보 반환")
      }
  )
  @PutMapping("/{playlistId}")
  ResponseEntity<PlaylistResponse> updatePlaylist(
      @Parameter(description = "수정할 플레이리스트 ID", required = true)
      @PathVariable("playlistId") UUID playlistId,

      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "플레이리스트 수정 요청 DTO",
          required = true,
          content = @Content(schema = @Schema(implementation = PlaylistUpdateRequest.class))
      )
      @RequestBody @Valid PlaylistUpdateRequest request
  );

  @Operation(
      summary = "플레이리스트 삭제",
      description = "플레이리스트를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "204", description = "성공적으로 삭제됨")
      }
  )
  @DeleteMapping("/{playlistId}")
  ResponseEntity<Void> deletePlaylist(
      @Parameter(description = "삭제할 플레이리스트 ID", required = true)
      @PathVariable("playlistId") UUID playlistId
  );

  @Operation(
      summary = "콘텐츠 추가",
      description = "플레이리스트에 콘텐츠를 추가합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "콘텐츠가 성공적으로 추가됨")
      }
  )
  @PostMapping("/{playlistId}/contents")
  ResponseEntity<Void> addContentToPlaylist(
      @Parameter(description = "콘텐츠를 추가할 플레이리스트 ID", required = true)
      @PathVariable("playlistId") UUID playlistId,

      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "추가할 콘텐츠 정보",
          required = true,
          content = @Content(schema = @Schema(implementation = PlaylistContentRequest.class))
      )
      @RequestBody @Valid PlaylistContentRequest request
  );

  @Operation(
      summary = "콘텐츠 제거",
      description = "플레이리스트에서 콘텐츠를 제거합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "콘텐츠가 성공적으로 제거됨")
      }
  )
  @DeleteMapping("/{playlistId}/contents")
  ResponseEntity<Void> removeContentFromPlaylist(
      @Parameter(description = "콘텐츠를 제거할 플레이리스트 ID", required = true)
      @PathVariable("playlistId") UUID playlistId,

      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "제거할 콘텐츠 정보",
          required = true,
          content = @Content(schema = @Schema(implementation = PlaylistContentRequest.class))
      )
      @RequestBody @Valid PlaylistContentRequest request
  );

  @Operation(
      summary = "플레이리스트 조회",
      description = "플레이리스트 상세 정보를 조회합니다. 비공개일 경우 소유자만 접근 가능합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "조회된 플레이리스트 정보 반환")
      }
  )
  @GetMapping("/{playlistId}")
  ResponseEntity<PlaylistResponse> getPlaylist(
      @Parameter(description = "조회할 플레이리스트 ID", required = true)
      @PathVariable("playlistId") UUID playlistId
  );
}
