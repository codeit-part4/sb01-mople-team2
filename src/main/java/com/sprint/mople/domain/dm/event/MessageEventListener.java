package com.sprint.mople.domain.dm.event;

import com.sprint.mople.domain.dm.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MessageEventListener {

  private final SimpMessagingTemplate simpMessagingTemplate;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleMessageCreatedEvent(MessageCreatedEvent event) {
    MessageResponse message = event.messageResponse();
    simpMessagingTemplate.convertAndSend("/sub/chatrooms/" + message.chatRoomId(), message);
  }
}
