package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistsPage;
import com.sprint.mople.domain.playlist.service.PlaylistRecommendService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/playlists/recommend")
@RequiredArgsConstructor
public class PlaylistRecommendController {

  private final PlaylistRecommendService playlistRecommendService;

  @Operation(summary = "카테고리 기반 추천 플레이리스트 조회 (커서 기반 페이징)")
  @GetMapping
  public ResponseEntity<RecommendedPlaylistsPage> getRecommendedPlaylists(
      @RequestParam List<String> categories,                     // 사용자가 선택한 카테고리
      @RequestParam(required = false) Double lastScore,          // 커서: 마지막 점수
      @RequestParam(required = false) UUID lastId,               // 커서: 마지막 ID
      @RequestParam(defaultValue = "10") @Min(1) int size,        // 페이지 사이즈
      @RequestParam(required = false) String query
  ) {
    RecommendedPlaylistsPage response = playlistRecommendService.getRecommendedPlaylists(
        categories, lastScore, lastId, size, query
    );
    return ResponseEntity.ok(response);
  }
}
