package com.sprint.mople.domain.playlist.dto;

import java.util.UUID;

public record SubscriberCountResponse(
    UUID playlistId,
    Long count
)
{
}
