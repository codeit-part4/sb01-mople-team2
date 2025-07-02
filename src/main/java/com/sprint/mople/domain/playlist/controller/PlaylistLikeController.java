package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.playlist.service.PlaylistLikeService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/playlists/{playlistId}/likes")
@RequiredArgsConstructor
class PlaylistLikeController implements PlaylistLikeApi {

  private final PlaylistLikeService playlistLikeService;
  private final HttpServletRequest request;
  private final JwtProvider jwtProvider;

  private UUID getRequestUserId() {
    String token = JwtTokenExtractor.resolveToken(request);
    return jwtProvider.getUserId(token);
  }

  @PostMapping
  public ResponseEntity<Void> like(@PathVariable UUID playlistId) {
    playlistLikeService.likePlaylist(getRequestUserId(), playlistId);
    return ResponseEntity
        .ok()
        .build();
  }

  @DeleteMapping
  public ResponseEntity<Void> unlike(@PathVariable UUID playlistId) {
    playlistLikeService.unlikePlaylist(getRequestUserId(), playlistId);
    return ResponseEntity
        .noContent()
        .build();
  }

  @GetMapping("/me")
  public ResponseEntity<Boolean> isLiked(@PathVariable UUID playlistId) {
    return ResponseEntity.ok(playlistLikeService.isLiked(getRequestUserId(), playlistId));
  }

  @GetMapping
  public ResponseEntity<Long> likeCount(@PathVariable UUID playlistId) {
    return ResponseEntity.ok(playlistLikeService.getLikeCount(playlistId));
  }
}

