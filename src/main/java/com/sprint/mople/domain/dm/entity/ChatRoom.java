package com.sprint.mople.domain.dm.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sprint.mople.domain.user.entity.User;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

  @Id
  @Column(name = "chat_room_id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToMany
  @JoinTable(
      name = "chatrooms-users",
      joinColumns = @JoinColumn(name = "chat_room_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private List<User> participants = new ArrayList<>();

  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages = new ArrayList<>();

  public ChatRoom(List<User> participants) {
    this.participants = participants;
  }
}
