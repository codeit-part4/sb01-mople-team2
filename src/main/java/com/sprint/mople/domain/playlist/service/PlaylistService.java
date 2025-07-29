package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.content.dto.ContentCardResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface PlaylistService {

  @Transactional
  PlaylistResponse createPlaylist(
      PlaylistCreateRequest request,
      UUID requestUserId
  );

  @Transactional
  PlaylistResponse updatePlaylist(
      UUID playlistId,
      PlaylistUpdateRequest request,
      UUID requestUserId
  );

  @Transactional
  void deletePlaylist(UUID playlistId, UUID requestUserId);

  @Transactional
  void addContent(UUID playlistId, PlaylistContentRequest request, UUID requestUserId);

  @Transactional
  void removeContent(
      UUID playlistId,
      PlaylistContentRequest request,
      UUID requestUserId
  );

  @Transactional(readOnly = true)
  PlaylistResponse getPlaylistById(UUID playlistId, UUID requestUserId);

  List<ContentCardResponse> getContentByPlaylist(UUID playlistId, UUID userId);

  List<PlaylistResponse> getAllPlaylists();

  List<PlaylistResponse> getPlaylistsByUserId(UUID userId);
}
