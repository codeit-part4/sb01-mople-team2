package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.content.dto.ContentCardResponse;
import com.sprint.mople.domain.playlist.api.PlaylistApi;
import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import com.sprint.mople.domain.playlist.service.PlaylistService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
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

  // TODO SecurityContext 대체
  private UUID getRequestUserId() {
    String token = JwtTokenExtractor.resolveToken(request);
    return jwtProvider.getUserId(token);
  }

  @Override
  public ResponseEntity<PlaylistResponse> createPlaylist(
      PlaylistCreateRequest req
  )
  {

    UUID userId = getRequestUserId();
    PlaylistResponse res = playlistService.createPlaylist(req, userId);
    return ResponseEntity.ok(res);
  }

  @Override
  public ResponseEntity<PlaylistResponse> updatePlaylist(UUID playlistId, PlaylistUpdateRequest req)
  {

    UUID userId = getRequestUserId();
    PlaylistResponse res = playlistService.updatePlaylist(playlistId, req, userId);
    return ResponseEntity.ok(res);
  }

  @Override
  public ResponseEntity<Void> deletePlaylist(UUID playlistId) {
    playlistService.deletePlaylist(playlistId, getRequestUserId());
    return ResponseEntity
        .noContent()
        .build();
  }

  @Override
  public ResponseEntity<Void> addContentToPlaylist(UUID playlistId, PlaylistContentRequest req)
  {

    playlistService.addContent(playlistId, req, getRequestUserId());
    return ResponseEntity
        .ok()
        .build();
  }

  @Override
  public ResponseEntity<Void> removeContentFromPlaylist(UUID playlistId, PlaylistContentRequest req)
  {

    playlistService.removeContent(playlistId, req, getRequestUserId());
    return ResponseEntity
        .ok()
        .build();
  }

  @Override
  public ResponseEntity<PlaylistResponse> getPlaylist(UUID playlistId) {
    PlaylistResponse res = playlistService.getPlaylistById(playlistId, UUID.randomUUID());
    //TODO:janghoosa 임시 userId로 조회
    return ResponseEntity.ok(res);
  }

  @Override
  public ResponseEntity<List<ContentCardResponse>> getPlaylistContents(
      UUID playlistId
  )
  {
    //TODO:janghoosa 임시 userId로 조회
    List<ContentCardResponse> contents = playlistService.getContentByPlaylist(
        playlistId,
        UUID.randomUUID()
    );
    return ResponseEntity.ok(contents);
  }

  @Override
  public ResponseEntity<List<PlaylistResponse>> getAllPlaylists() {
    List<PlaylistResponse> playlists = playlistService.getAllPlaylists();
    return ResponseEntity.ok(playlists);
  }

  @Override
  public ResponseEntity<List<PlaylistResponse>> getPlaylistsByUserId(UUID userId) {
    List<PlaylistResponse> playlists = playlistService.getPlaylistsByUserId(userId);
    return ResponseEntity.ok(playlists);
  }
}
