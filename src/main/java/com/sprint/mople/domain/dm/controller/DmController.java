package com.sprint.mople.domain.dm.controller;

import static com.sprint.mople.global.jwt.JwtTokenExtractor.extractUserId;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.service.ChatRoomService;
import com.sprint.mople.domain.dm.service.MessageService;
import com.sprint.mople.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/dm")
public class DmController implements DmApi {

  private final ChatRoomService chatRoomService;

  private final MessageService messageService;

  private final JwtProvider jwtProvider;

  @PostMapping("/{targetUserId}")
  public ResponseEntity<ChatRoomResponse> createChatRoom(@PathVariable UUID targetUserId,
      HttpServletRequest request) {
    UUID requestUserId = extractUserId(request, jwtProvider);
    log.debug("DM 채팅방 생성 요청: {}", targetUserId);
    ChatRoomResponse response = chatRoomService.createChatRoom(requestUserId, targetUserId);
    log.debug("DM 채팅방 생성 완료: {}", response);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{targetUserId}")
  public ResponseEntity<List<MessageResponse>> findAllMessages(@PathVariable UUID targetUserId,
      HttpServletRequest request) {
    UUID requestUserId = extractUserId(request, jwtProvider);
    log.debug("DM 채팅방 조회 요청: {}", targetUserId);
    List<MessageResponse> response = messageService.findAll(requestUserId, targetUserId);
    log.debug("{} 유저와의 전체 메세지 조회 완료: {}", targetUserId, response);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<ChatRoomResponse>> findAllChatRooms(HttpServletRequest request) {
    UUID userId = extractUserId(request, jwtProvider);
    log.debug("DM 채팅방 목록 조회 요청: {}", userId);
    Page<ChatRoomResponse> response = chatRoomService.findAllChatRooms(userId);
    return ResponseEntity.ok(response);
  }

}
