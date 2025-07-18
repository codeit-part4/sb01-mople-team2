package com.sprint.mople.domain.playlist.dto;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.user.dto.UserListResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RecommendedPlaylistResponse(
    UUID id,
    UserListResponse user,
    String title,
    String description,
    boolean isPublic,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    int subscribeCount,

    @Schema(description = "추천 점수", example = "3.75")
    double score
) {

  public static RecommendedPlaylistResponse from(Playlist playlist, double score) {
    return new RecommendedPlaylistResponse(
        playlist.getId(),
        UserListResponse.from(playlist.getUser()),
        playlist.getTitle(),
        playlist.getDescription(),
        playlist.getIsPublic(),
        playlist.getCreatedAt(),
        playlist.getUpdatedAt(),
        playlist.getSubscriptions().size(),
        score
    );
  }
}

