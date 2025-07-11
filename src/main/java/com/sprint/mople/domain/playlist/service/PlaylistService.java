package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
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
}
