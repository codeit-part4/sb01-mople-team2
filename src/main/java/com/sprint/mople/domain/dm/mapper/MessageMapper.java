package com.sprint.mople.domain.dm.mapper;

import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {
  public MessageResponse toDto(Message message){
    return new MessageResponse(
        message.getSender().getId(),
        message.getChatRoom().getId(),
        message.getSender().getId(),
        message.getContent(),
        message.getCreatedAt()
    );
  }

}
