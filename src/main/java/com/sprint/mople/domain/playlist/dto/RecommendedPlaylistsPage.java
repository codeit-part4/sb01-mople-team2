package com.sprint.mople.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

public record RecommendedPlaylistsPage(
    List<RecommendedPlaylistResponse> playlists,

    @Schema(description = "다음 페이지 조회를 위한 커서: 추천 점수")
    Double nextScore,

    @Schema(description = "다음 페이지 조회를 위한 커서: 플레이리스트 ID")
    UUID nextId
) {
  public static RecommendedPlaylistsPage from(List<RecommendedPlaylistResponse> list) {
    if (list.isEmpty()) {
      return new RecommendedPlaylistsPage(List.of(), null, null);
    }

    RecommendedPlaylistResponse last = list.get(list.size() - 1);
    return new RecommendedPlaylistsPage(list, last.score(), last.id());
  }
}
