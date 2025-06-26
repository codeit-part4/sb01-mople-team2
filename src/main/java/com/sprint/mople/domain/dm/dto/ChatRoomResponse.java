package com.sprint.mople.domain.dm.dto;

import java.util.List;
import java.util.UUID;

public record ChatRoomResponse(
    List<UUID> participantIds
) {

}
