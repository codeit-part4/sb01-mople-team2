package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.ChatRoomNotFoundException;
import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.Message;
import com.sprint.mople.domain.dm.mapper.MessageMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.dm.repository.ChatRoomUserRepository;
import com.sprint.mople.domain.dm.repository.MessageRepository;
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
public class MessageServiceImpl implements MessageService{

  private final MessageRepository messageRepository;
  private final ChatRoomUserRepository chatRoomUserRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;
  private final ChatRoomService chatRoomService;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageResponse create(UUID senderId, UUID receiverId, String content) {
    log.debug("메세지 생성 시작 - 보낸이: {}, 받는이: {}, 내용: {}", senderId, receiverId, content);
    User sender = userRepository.findById(senderId)
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    ChatRoom chatRoom = chatRoomUserRepository.findChatRoomByUserIds(senderId, receiverId)
        .orElseGet(()->  {
          ChatRoomResponse chatRoomResponse = chatRoomService.createChatRoom(senderId, receiverId);
          return chatRoomRepository.findById(chatRoomResponse.id())
              .orElseThrow(ChatRoomNotFoundException::new);
        });
    Message message = new Message(chatRoom, sender, content);
    messageRepository.save(message);
    log.debug("메세지 생성 완료 - 메세지 ID: {}", message.getId());
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public List<MessageResponse> findAll(UUID requestUserId, UUID targetUserId) {
    log.debug("DM 전체 메세지 조회 시작 - 요청 유저: {}, 대상 유저: {}", requestUserId, targetUserId);
    ChatRoom chatRoom = chatRoomUserRepository.findChatRoomByUserIds(requestUserId, targetUserId)
        .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

    List<Message> messages = messageRepository.findAllByChatRoomId(chatRoom.getId());
    return messages.stream().map(messageMapper::toDto).toList();
  }
}
