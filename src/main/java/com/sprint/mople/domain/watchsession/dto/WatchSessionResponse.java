package com.sprint.mople.domain.watchsession.dto;

import com.sprint.mople.domain.watchsession.entity.WatchSession;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record WatchSessionResponse(
    @Schema(description = "시청 세션 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "연결된 콘텐츠 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID contentId,

    @Schema(description = "시청 세션 생성 시각")
    Instant createdAt,

    @Schema(description = "시청 세션 수정 시각")
    Instant updatedAt,

    @Schema(description = "현재 세션에 참여 중인 사용자 수")
    int participantCount
) {
    public static WatchSessionResponse from(WatchSession session, int participantCount) {
        return WatchSessionResponse.builder()
            .id(session.getId())
            .contentId(session.getContent().getId())
            .createdAt(session.getCreatedAt())
            .updatedAt(session.getUpdatedAt())
            .participantCount(participantCount)
            .build();
    }
}