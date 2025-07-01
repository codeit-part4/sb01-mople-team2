package com.sprint.mople.domain.dm.event;

import com.sprint.mople.domain.dm.dto.MessageResponse;

public record MessageCreatedEvent(
    MessageResponse messageResponse
) {

}
