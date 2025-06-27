package com.sprint.mople.domain.dm.controller;

import com.sprint.mople.domain.dm.dto.MessageCreateRequest;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

  private final SimpMessagingTemplate simpMessagingTemplate;

  private final MessageService messageService;

  @MessageMapping("/messages")
  public void sendMessage(MessageCreateRequest request) {
    MessageResponse response = messageService.create(request);
    simpMessagingTemplate.convertAndSend("/sub/chatrooms/" + response.chatRoomId(), response);
  }

}
