package com.sprint.mople.domain.content.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;

public record ContentResponse(
    @Schema(description = "콘텐츠 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "콘텐츠 제목", example = "제목")
    String title,

    @Schema(description = "콘텐츠 좋아요 수", example = "42")
    long likeCount,

    @Schema(description = "콘텐츠 생성 시간")
    Instant createdAt,

    @Schema(description = "콘텐츠 수정 시간")
    Instant updatedAt,

    @Schema(description = "사용자의 좋아요 여부", example = "true")
    boolean liked
)
{
}
