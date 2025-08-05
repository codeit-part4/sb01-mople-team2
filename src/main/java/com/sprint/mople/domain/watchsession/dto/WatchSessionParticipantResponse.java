package com.sprint.mople.domain.watchsession.dto;

import com.sprint.mople.domain.watchsession.entity.WatchSessionParticipant;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record WatchSessionParticipantResponse(
    @Schema(description = "참여자 ID")
    UUID participantId,

    @Schema(description = "유저 ID")
    UUID userId,

    @Schema(description = "유저 이름")
    String userName,

    @Schema(description = "세션 참여 시각")
    Instant joinedAt
) {
  public static WatchSessionParticipantResponse from(WatchSessionParticipant participant) {
    return WatchSessionParticipantResponse.builder()
        .participantId(participant.getId())
        .userId(participant.getUser().getId())
        .userName(participant.getUser().getUserName())
        .joinedAt(participant.getJoinedAt())
        .build();
  }
}
