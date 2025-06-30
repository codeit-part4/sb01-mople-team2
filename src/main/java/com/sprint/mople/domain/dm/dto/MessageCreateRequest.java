package com.sprint.mople.domain.dm.dto;

import java.util.UUID;

public record MessageCreateRequest(
    UUID senderId,
    UUID receiverId,
    String content
) {

}
