package com.sprint.mople.domain.dm.controller;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.service.ChatRoomService;
import com.sprint.mople.domain.dm.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController {

  private final ChatRoomService chatRoomService;

  private final MessageService messageService;

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

  @GetMapping("/{targetUserId}")
  public ResponseEntity<List<MessageResponse>> getChatRoom(@PathVariable UUID targetUserId,
      //    @AuthenticationPrincipal MopleUserDetails userDetails, TODO: MopleUserDetails 구현 필요
      @RequestHeader("X-USER-ID") UUID requestUserId) {
    //    UUID requestUserId = userDetails.getId();
    log.debug("DM 채팅방 조회 요청: {}", targetUserId);
    List<MessageResponse> response = messageService.findAll(requestUserId, targetUserId);
    log.debug("{} 유저와의 전체 메세지 조회 완료: {}", targetUserId, response);
    return ResponseEntity.ok(response);
  }


}
