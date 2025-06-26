package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.mapper.ChatRoomMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService{

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final ChatRoomMapper chatRoomMapper;

  @Transactional
  @Override
  public ChatRoomResponse createChatRoom(UUID requestUserId, UUID targetUserId) {
    log.debug("DM 채팅방 생성 시작 - 유저: {}, 대상: {}", requestUserId, targetUserId);
    User requestUser = userRepository.findById(requestUserId)
        .orElseThrow(() -> new IllegalArgumentException("요청 유저를 찾을 수 없습니다 - id: " + requestUserId));
    User targetUser = userRepository.findById(targetUserId)
        .orElseThrow(() -> new IllegalArgumentException("대상 유저를 찾을 수 없습니다 - id: " + targetUserId));
    ChatRoom chatRoom = new ChatRoom(List.of(requestUser, targetUser));
    chatRoomRepository.save(chatRoom);
    log.debug("DM 채팅방 생성 완료 - 채팅방 ID: {}", chatRoom.getId());
    return chatRoomMapper.toDto(chatRoom);
  }
}
