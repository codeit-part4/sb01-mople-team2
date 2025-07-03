package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import com.sprint.mople.domain.playlist.service.PlaylistService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaylistController implements PlaylistApi {

  private final PlaylistService playlistService;
  private final JwtProvider jwtProvider;
  private final HttpServletRequest request;

  private UUID getRequestUserId() {
    String token = JwtTokenExtractor.resolveToken(request);
    return jwtProvider.getUserId(token);
  }

  @Override
  public ResponseEntity<PlaylistResponse> createPlaylist(
      UUID playlistId,
      PlaylistCreateRequest req) {

    UUID userId = getRequestUserId();
    PlaylistResponse res = playlistService.createPlaylist(playlistId, req, userId);
    return ResponseEntity.ok(res);
  }

  @Override
  public ResponseEntity<PlaylistResponse> updatePlaylist(
      UUID playlistId,
      PlaylistUpdateRequest req) {

    UUID userId = getRequestUserId();
    PlaylistResponse res = playlistService.updatePlaylist(playlistId, req, userId);
    return ResponseEntity.ok(res);
  }

  @Override
  public ResponseEntity<Void> deletePlaylist(UUID playlistId) {
    playlistService.deletePlaylist(playlistId, getRequestUserId());
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<Void> addContentToPlaylist(
      UUID playlistId,
      PlaylistContentRequest req) {

    playlistService.addContent(playlistId, req, getRequestUserId());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> removeContentFromPlaylist(
      UUID playlistId,
      PlaylistContentRequest req) {

    playlistService.removeContent(playlistId, req, getRequestUserId());
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<PlaylistResponse> getPlaylist(UUID playlistId) {
    PlaylistResponse res =
        playlistService.getPlaylistById(playlistId, getRequestUserId());
    return ResponseEntity.ok(res);
  }
}