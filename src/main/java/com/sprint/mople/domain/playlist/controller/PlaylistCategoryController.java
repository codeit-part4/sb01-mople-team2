package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.playlist.dto.PlaylistCategoryRequest;
import com.sprint.mople.domain.playlist.entity.PlaylistCategory;
import com.sprint.mople.domain.playlist.service.PlaylistCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Playlist Category", description = "플레이리스트 카테고리 관련 API")
public class PlaylistCategoryController {

  private final PlaylistCategoryService playlistCategoryService;

  @Operation(summary = "카테고리 생성", description = "새로운 플레이리스트 카테고리를 생성합니다.")
  @PostMapping
  public ResponseEntity<PlaylistCategory> createCategory(
      @Parameter(description = "생성할 카테고리 이름", required = true)
      @RequestParam PlaylistCategoryRequest dto) {
    PlaylistCategory created = playlistCategoryService.createCategory(dto);
    return ResponseEntity.ok(created);
  }

  @Operation(summary = "카테고리 수정", description = "기존 카테고리의 이름을 수정합니다.")
  @PutMapping("/{categoryId}")
  public ResponseEntity<PlaylistCategory> updateCategory(
      @Parameter(description = "수정할 카테고리 ID", required = true)
      @PathVariable Long categoryId,
      @Parameter(description = "새로운 카테고리 이름", required = true)
      @RequestParam PlaylistCategoryRequest dto) {
    PlaylistCategory updated = playlistCategoryService.updateCategory(categoryId, dto);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
  @DeleteMapping("/{categoryId}")
  public ResponseEntity<Void> deleteCategory(
      @Parameter(description = "삭제할 카테고리 ID", required = true)
      @PathVariable Long categoryId) {
    playlistCategoryService.deleteCategory(categoryId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "카테고리 전체 조회", description = "등록된 모든 카테고리를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<PlaylistCategory>> getAllCategories() {
    return ResponseEntity.ok(playlistCategoryService.findAllCategories());
  }

  @Operation(summary = "플레이리스트에 카테고리 추가", description = "특정 플레이리스트에 카테고리를 연결합니다.")
  @PostMapping("/{categoryId}/playlists/{playlistId}")
  public ResponseEntity<Void> addCategoryToPlaylist(
      @Parameter(description = "카테고리 ID", required = true)
      @PathVariable Long categoryId,
      @Parameter(description = "플레이리스트 UUID", required = true)
      @PathVariable UUID playlistId) {
    playlistCategoryService.addCategoryToPlaylist(playlistId, categoryId);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "플레이리스트에서 카테고리 제거", description = "특정 플레이리스트에서 카테고리 연결을 제거합니다.")
  @DeleteMapping("/{categoryId}/playlists/{playlistId}")
  public ResponseEntity<Void> removeCategoryFromPlaylist(
      @Parameter(description = "카테고리 ID", required = true)
      @PathVariable Long categoryId,
      @Parameter(description = "플레이리스트 UUID", required = true)
      @PathVariable UUID playlistId) {
    playlistCategoryService.removeCategoryFromPlaylist(playlistId, categoryId);
    return ResponseEntity.noContent().build();
  }
}
