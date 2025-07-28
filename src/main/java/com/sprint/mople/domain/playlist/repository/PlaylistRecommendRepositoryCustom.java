package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistResponse;
import com.sprint.mople.domain.playlist.entity.PlaylistSortType;
import java.util.List;
import java.util.UUID;

public interface PlaylistRecommendRepositoryCustom {
  List<RecommendedPlaylistResponse> findRecommendedByUserCategoriesWithCursor(
      List<String> userCategories,
      Double lastScore,
      UUID lastId,
      int pageSize,
      String query,
      PlaylistSortType searchType
  );
}

