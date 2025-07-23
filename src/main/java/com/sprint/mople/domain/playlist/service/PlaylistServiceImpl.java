package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.content.dto.ContentCardResponse;
import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.playlist.dto.PlaylistContentRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistCreateRequest;
import com.sprint.mople.domain.playlist.dto.PlaylistResponse;
import com.sprint.mople.domain.playlist.dto.PlaylistUpdateRequest;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistContent;
import com.sprint.mople.domain.playlist.exception.DuplicatePlaylistContentException;
import com.sprint.mople.domain.playlist.exception.PlaylistContentNotFoundException;
import com.sprint.mople.domain.playlist.exception.PlaylistIllegalAccessException;
import com.sprint.mople.domain.playlist.exception.PlaylistNotFoundException;
import com.sprint.mople.domain.playlist.repository.PlaylistCategoryMappingRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.UserNotFoundException;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistServiceImpl implements PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final UserRepository userRepository;
  private final ContentRepository contentRepository;
  private final PlaylistCategoryMappingRepository playlistCategoryMappingRepository;

  @Transactional
  @Override
  public PlaylistResponse createPlaylist(
      PlaylistCreateRequest request,
      UUID requestUserId
  )
  {

    log.info("Create playlist: requestUserId={}", requestUserId);

    User owner = userRepository
        .findById(requestUserId)
        .orElseThrow(UserNotFoundException::new);

    Playlist playlist = Playlist
        .builder()
        .user(owner)
        .title(request.title())
        .description(request.description())
        .isPublic(request.isPublic())
        .build();

    Playlist saved = playlistRepository.save(playlist);
    log.debug("Playlist saved: {}", saved.getId());

    return PlaylistResponse.from(saved);
  }

  @Transactional
  @Override
  public PlaylistResponse updatePlaylist(
      UUID playlistId,
      PlaylistUpdateRequest request,
      UUID requestUserId
  )
  {

    Playlist playlist = getOwnedPlaylist(playlistId, requestUserId);

    log.info("Update playlist: playlistId={}, by user={}", playlistId, requestUserId);

    playlist.update(request);

    return PlaylistResponse.from(playlist);
  }

  @Transactional
  @Override
  public void deletePlaylist(UUID playlistId, UUID requestUserId) {
    Playlist playlist = getOwnedPlaylist(playlistId, requestUserId);
    playlistRepository.delete(playlist);
    log.info("Deleted playlist: {} by user={}", playlistId, requestUserId);
  }

  @Transactional
  @Override
  public void addContent(UUID playlistId, PlaylistContentRequest request, UUID requestUserId)
  {
    Playlist playlist = getOwnedPlaylist(playlistId, requestUserId);
    Content content = contentRepository
        .findById(request.contentId())
        .orElseThrow(() -> new IllegalArgumentException("콘텐츠가 없습니다."));

    boolean alreadyExists = playlist
        .getPlaylistContents()
        .stream()
        .anyMatch(pc -> pc
            .getContent()
            .getId()
            .equals(content.getId()));
    if (alreadyExists) {
      log.warn("Content {} already in playlist {}", content.getId(), playlistId);
      throw new DuplicatePlaylistContentException();
    }

    PlaylistContent playlistContent = new PlaylistContent();
    playlistContent.setPlaylist(playlist);
    playlistContent.setContent(content);

//    playlist
//        .getPlaylistContents()
//        .add(playlistContent);
//    content
//        .getPlaylistContents()
//        .add(playlistContent);
    log.info("Added content {} to playlist {}", content.getId(), playlistId);
  }

  @Transactional
  @Override
  public void removeContent(
      UUID playlistId,
      PlaylistContentRequest request,
      UUID requestUserId
  )
  {

    Playlist playlist = getOwnedPlaylist(playlistId, requestUserId);

    Content content = contentRepository
        .findById(request.contentId())
        .orElseThrow(() -> new IllegalArgumentException("콘텐츠가 없습니다."));

    PlaylistContent link = playlist
        .getPlaylistContents()
        .stream()
        .filter(pc -> pc
            .getContent()
            .getId()
            .equals(content.getId()))
        .findFirst()
        .orElseThrow(PlaylistContentNotFoundException::new);

    playlist
        .getPlaylistContents()
        .remove(link);
    content
        .getPlaylistContents()
        .remove(link);

    log.info("Removed content {} from playlist {}", content.getId(), playlistId);
  }

  @Transactional(readOnly = true)
  @Override
  public PlaylistResponse getPlaylistById(UUID playlistId, UUID requestUserId) {
    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);

    if (!playlist.getIsPublic() && !playlist
        .getUser()
        .getId()
        .equals(requestUserId)) {
      throw new PlaylistIllegalAccessException();
    }
    return PlaylistResponse.from(playlist);
  }

  @Override
  public List<ContentCardResponse> getContentByPlaylist(UUID playlistId, UUID userId) {
    // userId는 현재 사용하지 않지만, 추후에 사용자 권한 체크를 위해 남겨둠
    Playlist playlist = playlistRepository.findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);

    List<UUID> contentIds = playlist.getPlaylistContents().stream()
        .map(pc -> pc.getContent().getId())
        .toList();

    List<Content> contents = contentRepository.findAllByIdIn(contentIds);

    return contents.stream()
        .map(ContentCardResponse::from)
        .toList();
  }

  @Override
  public List<PlaylistResponse> getAllPlaylists(){
    List<Playlist> playlists = playlistRepository.findAllByIsPublicTrue();
    return playlists.stream()
        .map(PlaylistResponse::from)
        .toList();
  }

  private Playlist getOwnedPlaylist(UUID playlistId, UUID requestUserId) {
    Playlist playlist = playlistRepository
        .findById(playlistId)
        .orElseThrow(PlaylistNotFoundException::new);

    if (!playlist
        .getUser()
        .getId()
        .equals(requestUserId)) {
      throw new PlaylistIllegalAccessException();
    }
    return playlist;
  }
}
