package com.sprint.mople.domain.dm.controller;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "DM", description = "DM 관련 API")
public interface DmApi {

  @Operation(summary = "채팅방 생성", description = "특정 사용자와의 채팅방을 생성합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "채팅방이 성공적으로 생성됨"
      )
  })
  ResponseEntity<ChatRoomResponse> createChatRoom(@PathVariable UUID targetUserId,
      HttpServletRequest request);

  @Operation(summary = "채팅 조회", description = "특정 사용자와의 모든 메세지를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "메세지를 성공적으로 조회함")
  })
  ResponseEntity<List<MessageResponse>> findAllMessages(@PathVariable UUID targetUserId,
      HttpServletRequest request);

  @Operation(summary = "채팅방 목록 조회", description = "사용자의 모든 채팅방을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "채팅방 목록을 성공적으로 조회함")
  })
  ResponseEntity<Page<ChatRoomResponse>> findAllChatRooms(HttpServletRequest request);
}
