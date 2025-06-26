package com.sprint.mople.domain.dm.controller;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.service.ChatRoomService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @PostMapping("/{targetUserId}")
  public ResponseEntity<ChatRoomResponse> createChatRoom(@PathVariable UUID targetUserId,
//      @AuthenticationPrincipal MopleUserDetails userDetails, TODO: MopleUserDetails 구현 필요
      @RequestHeader("X-USER-ID") UUID requestUserId) {
    //    UUID requestUserId = userDetails.getId();
    log.debug("DM 채팅방 생성 요청: {}", targetUserId);
    ChatRoomResponse response = chatRoomService.createChatRoom(requestUserId, targetUserId);
    log.debug("DM 채팅방 생성 완료: {}", response);
    return ResponseEntity.ok(response);
  }

}
