package com.sprint.mople.domain.dm.mapper;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

  public ChatRoomResponse toDto(ChatRoom chatRoom){
    return new ChatRoomResponse(chatRoom.getParticipants().stream()
        .map(User::getId)
        .toList());
  }
}
