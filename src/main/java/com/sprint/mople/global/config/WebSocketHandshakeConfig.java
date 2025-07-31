package com.sprint.mople.global.config;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
public class WebSocketHandshakeConfig {

  @Bean
  public DefaultHandshakeHandler handshakeHandler() {
    return new DefaultHandshakeHandler() {
      @Override
      protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        return new Principal() {
          @Override
          public String getName() {
            return UUID.randomUUID().toString();
          }
        };
      }
    };
  }
}