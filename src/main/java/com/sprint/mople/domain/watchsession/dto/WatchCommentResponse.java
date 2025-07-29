package com.sprint.mople.domain.watchsession.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record WatchCommentResponse(
    @Schema(description = "댓글 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "작성자 ID", example = "123e4567-e89b-12d3-a456-426614174001")
    UUID userId,

    @Schema(description = "시청 세션 ID", example = "123e4567-e89b-12d3-a456-426614174002")
    UUID sessionId,

    @Schema(description = "댓글 내용")
    String message,

    @Schema(description = "댓글 작성 시각")
    Instant sentAt
) {}