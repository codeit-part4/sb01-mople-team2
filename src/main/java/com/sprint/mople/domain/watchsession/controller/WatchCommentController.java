package com.sprint.mople.domain.watchsession.controller;

import com.sprint.mople.domain.watchsession.dto.WatchCommentResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WatchCommentController {

  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/watch-session/{sessionId}/comments")
  public void handleComment(
      @DestinationVariable UUID sessionId,
      @Valid WatchCommentResponse comment,
      Principal principal
  ) {
    log.debug("🔐 WebSocket 연결된 사용자 정보: {}", principal);
    log.debug("👤 인증된 사용자 ID (principal.getName()): {}", principal.getName());
    log.debug("세션 {} 에 댓글 도착: {}", sessionId, comment);

    messagingTemplate.convertAndSend(
        "/sub/watch-session/" + sessionId,
        comment
    );
  }
}
