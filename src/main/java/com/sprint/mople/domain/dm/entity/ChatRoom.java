package com.sprint.mople.domain.dm.entity;

import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor
public class ChatRoom {

  @Id
  @Column(name = "chat_room_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ChatRoomUser> participants = new ArrayList<>();

  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  public ChatRoom(User user1, User user2) {
    this.participants.add(new ChatRoomUser(this, user1));
    this.participants.add(new ChatRoomUser(this, user2));  }
}
