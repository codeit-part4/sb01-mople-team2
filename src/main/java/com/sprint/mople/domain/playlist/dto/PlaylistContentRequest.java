package com.sprint.mople.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 플레이리스트 콘텐츠 추가 요청 DTO
 *
 * @param contentId
 */
public record PlaylistContentRequest(
    @NotNull @Schema(
        description = "콘텐츠 ID",
        example = "123e4567-e89b-12d3-a456-426614174000"
    ) UUID contentId
) {}