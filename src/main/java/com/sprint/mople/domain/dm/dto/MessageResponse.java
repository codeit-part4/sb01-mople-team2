package com.sprint.mople.domain.dm.dto;

import java.util.UUID;

public record MessageResponse(
    UUID chatRoomId,
    UUID senderId,
    String content
) {

}
