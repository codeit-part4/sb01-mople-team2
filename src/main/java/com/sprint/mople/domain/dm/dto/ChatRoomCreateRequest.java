package com.sprint.mople.domain.dm.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record ChatRoomCreateRequest(
    @Size(min = 2, max = 2, message = "DM은 2명만 참여할 수 있습니다.")
    List<UUID> participantIds
) {

}
