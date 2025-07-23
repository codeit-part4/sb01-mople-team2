package com.sprint.mople.domain.dm.controller;

import com.sprint.mople.domain.dm.dto.MessageCreateRequest;
import com.sprint.mople.domain.dm.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @MessageMapping("/messages")
  public void sendMessage(@Valid MessageCreateRequest request) {
    log.debug("메시지 전송 요청 - 보낸 사람: {}, 내용: {}", request.senderId(), request.content());
    messageService.create(request);
  }

}
