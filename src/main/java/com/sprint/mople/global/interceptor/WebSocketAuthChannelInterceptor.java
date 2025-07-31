package com.sprint.mople.global.interceptor;

import com.sprint.mople.global.jwt.JwtProvider;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

  private final JwtProvider jwtProvider;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    StompCommand command = accessor.getCommand();

    if (StompCommand.CONNECT.equals(command)) {
      log.info("🛜 WebSocket CONNECT 요청 감지됨");

      String rawHeader = accessor.getFirstNativeHeader("Authorization");
      if (rawHeader != null && rawHeader.startsWith("Bearer ")) {
        String token = rawHeader.substring(7);

        if (jwtProvider.validateToken(token)) {
          UUID userId = jwtProvider.extractUserId(token);
          log.info("🔐 인증 완료 — 사용자 ID: {}", userId);

          Authentication authentication = new UsernamePasswordAuthenticationToken(
              userId.toString(), null, List.of()
          );

          accessor.setUser(authentication);

        } else {
          log.warn("⚠️ 토큰 검증 실패 — 메시지는 계속 처리됨");
        }
      } else {
        log.warn("⚠️ Authorization 헤더가 없거나 잘못된 형식");
      }
    }

    return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
  }
}