package com.sprint.mople.domain.dm.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@IdClass(ChatRoomUserId.class)
@Table(name = "chatrooms_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public ChatRoomUser(ChatRoom chatRoom, User user) {
    this.chatRoom = chatRoom;
    this.user = user;
  }
}
