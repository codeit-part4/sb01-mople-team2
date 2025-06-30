package com.sprint.mople.domain.dm.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ChatRoomUserId implements Serializable {
  private UUID chatRoom;
  private UUID user;

  public ChatRoomUserId() {}

  public ChatRoomUserId(UUID chatRoom, UUID user) {
    this.chatRoom = chatRoom;
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChatRoomUserId)) return false;
    ChatRoomUserId that = (ChatRoomUserId) o;
    return Objects.equals(chatRoom, that.chatRoom) &&
        Objects.equals(user, that.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(chatRoom, user);
  }
}
