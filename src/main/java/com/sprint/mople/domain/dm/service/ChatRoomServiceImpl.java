package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.mapper.ChatRoomMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int DEFAULT_PAGE_NUMBER = 0;

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
    ChatRoom chatRoom = new ChatRoom(requestUser, targetUser);
    chatRoomRepository.save(chatRoom);
    log.debug("DM 채팅방 생성 완료 - 채팅방 ID: {}", chatRoom.getId());
    return chatRoomMapper.toDto(chatRoom);
  }

  @Override
  public Page<ChatRoomResponse> findAllChatRooms(UUID userId) {
    int page = DEFAULT_PAGE_NUMBER;
    int size = DEFAULT_PAGE_SIZE;
    log.debug("DM 채팅방 목록 조회 시작 - 유저: {}", userId);
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    Page<ChatRoom> chatRooms = chatRoomRepository.findAllByParticipantId(userId, pageable);
    if (chatRooms.isEmpty()) {
      log.info("DM 채팅방 목록 조회 결과 없음 - 유저: {}", userId);
      return Page.empty();
    }
    log.debug("DM 채팅방 목록 조회 완료 - 채팅방 수: {}", chatRooms.getTotalElements());
    return chatRooms.map(chatRoomMapper::toDto);
  }
}
