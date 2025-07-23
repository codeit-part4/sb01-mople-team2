package com.sprint.mople.domain.dm.dto;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
    UUID id,
    UUID chatRoomId,
    UUID senderId,
    String content,
    Instant createdAt
) {

}
