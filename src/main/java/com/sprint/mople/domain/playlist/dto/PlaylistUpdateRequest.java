package com.sprint.mople.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 플레이리스트 수정 요청 DTO
 *
 * @param title       플레이리스트 제목
 * @param description 플레이리스트 설명
 * @param isPublic    플레이리스트 공개여부
 */
@Schema(description = "플레이리스트 수정 요청 DTO")
public record PlaylistUpdateRequest(

    @Schema(description = "플레이리스트 제목", example = "내 최애 영화 모음", nullable = true) String title,

    @Schema(
        description = "플레이리스트 설명",
        example = "재밌게 본 영화들을 정리한 리스트예요.",
        nullable = true
    ) String description,

    @Schema(description = "플레이리스트 공개 여부", example = "true", nullable = true) Boolean isPublic
)
{}