package com.sprint.mople.domain.dm.mapper;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

  public ChatRoomResponse toDto(ChatRoom chatRoom){
    return new ChatRoomResponse(chatRoom.getId(), chatRoom.getParticipants().stream()
        .map(chatRoomUser -> chatRoomUser.getUser().getId())
        .toList());
  }
}
