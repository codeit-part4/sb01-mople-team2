package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.Message;
import com.sprint.mople.domain.dm.mapper.MessageMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.dm.repository.ChatRoomUserRepository;
import com.sprint.mople.domain.dm.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

  private final MessageRepository messageRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final MessageMapper messageMapper;

  @Override
  public MessageResponse create(UUID senderId, String content) {
    return null;
  }

  @Override
  public List<MessageResponse> findAll(UUID requestUserId, UUID targetUserId) {
    log.debug("DM 전체 메세지 조회 시작 - 요청 유저: {}, 대상 유저: {}", requestUserId, targetUserId);
    UUID chatRoomId = chatRoomUserRepository.findChatRoomIdByUserIds(requestUserId, targetUserId)
        .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
        .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
    List<Message> messages = messageRepository.findAllByChatRoomId(chatRoom.getId());
    return messages.stream().map(messageMapper::toDto).toList();
  }
}
