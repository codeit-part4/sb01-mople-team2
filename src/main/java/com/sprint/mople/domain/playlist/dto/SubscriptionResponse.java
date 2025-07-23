package com.sprint.mople.domain.playlist.dto;

import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.user.dto.UserListResponse;

public record SubscriptionResponse(
    Long id,

    UserListResponse user,

    PlaylistResponse playlist
)
{

  public static SubscriptionResponse from(
      Long id,
      UserListResponse user,
      PlaylistResponse playlist
  )
  {
    return new SubscriptionResponse(id, user, playlist);
  }

  public static SubscriptionResponse from(Subscription subscription) {
    return new SubscriptionResponse(
        subscription.getId(),
        UserListResponse.from(subscription.getUser()),
        PlaylistResponse.from(subscription.getPlaylist())
    );
  }
}
