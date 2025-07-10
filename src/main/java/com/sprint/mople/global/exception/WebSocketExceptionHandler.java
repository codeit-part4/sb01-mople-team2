package com.sprint.mople.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class WebSocketExceptionHandler {

  @MessageExceptionHandler(Exception.class)
  public void handleWebSocketException(Exception e){
    log.error("WebSocket 예외 발생: {}", e.getMessage(), e);
  }

}
