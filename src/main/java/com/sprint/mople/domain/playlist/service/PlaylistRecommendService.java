package com.sprint.mople.domain.playlist.service;

import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistResponse;
import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistsPage;
import com.sprint.mople.domain.playlist.repository.PlaylistRecommendRepository;
import com.sprint.mople.domain.playlist.repository.PlaylistRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaylistRecommendService {

  private final PlaylistRepository playlistRepository;
  private final PlaylistRecommendRepository playlistRecommendRepository;

  public RecommendedPlaylistsPage getRecommendedPlaylists(
      List<String> userCategories,
      Double lastScore,
      UUID lastId,
      int size
  )
  {
    List<RecommendedPlaylistResponse> list = playlistRecommendRepository.findRecommendedByUserCategoriesWithCursor(
        userCategories, lastScore, lastId, size
    );

    return RecommendedPlaylistsPage.from(list);
  }
}
