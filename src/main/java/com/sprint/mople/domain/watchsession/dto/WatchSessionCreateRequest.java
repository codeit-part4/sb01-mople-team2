package com.sprint.mople.domain.watchsession.dto;

import java.util.UUID;

public record WatchSessionCreateRequest(
    UUID contentId
) {

}
