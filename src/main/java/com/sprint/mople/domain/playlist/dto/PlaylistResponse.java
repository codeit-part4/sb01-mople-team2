package com.sprint.mople.domain.playlist.dto;

import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.user.dto.UserListResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * 플레이리스트 정보를 반환하기 위한 응답 DTO
 *
 * @param id             플레이리스트의 고유 식별자
 * @param user           플레이리스트를 생성한 사용자 정보 (요약 정보)
 * @param title          플레이리스트 제목
 * @param description    플레이리스트 설명
 * @param isPublic       공개 여부 (true: 공개, false: 비공개)
 * @param createdAt      플레이리스트 생성 시각 (ISO 8601 형식)
 * @param updatedAt      플레이리스트 마지막 수정 시각 (ISO 8601 형식)
 * @param subscribeCount 해당 플레이리스트를 구독 중인 사용자 수
 */
public record PlaylistResponse(
    @Schema(description = "플레이리스트 ID", example = "8c36d5ca-06c6-4e93-9f4c-4c5f4919d83d")
    UUID id,

    @Schema(description = "플레이리스트 생성자 정보 (요약)")
    UserListResponse user,

    @Schema(description = "플레이리스트 제목", example = "내가 사랑한 영화들")
    String title,

    @Schema(description = "플레이리스트 설명", example = "감성적인 영화들을 모아봤어요.")
    String description,

    @Schema(description = "공개 여부", example = "true")
    Boolean isPublic,

    @Schema(description = "생성 일시", example = "2024-06-01T13:00:00+09:00")
    OffsetDateTime createdAt,

    @Schema(description = "수정 일시", example = "2024-06-20T18:45:00+09:00")
    OffsetDateTime updatedAt,

    @Schema(description = "구독자 수", example = "23")
    int subscribeCount
)
{

  public static PlaylistResponse from(Playlist playlist) {
    return new PlaylistResponse(
        playlist.getId(),
        UserListResponse.from(playlist.getUser()),
        playlist.getTitle(),
        playlist.getDescription(),
        playlist.getIsPublic(),
        playlist.getCreatedAt(),
        playlist.getUpdatedAt(),
        playlist
            .getSubscribes()
            .size()
    );
  }
}
