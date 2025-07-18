package com.sprint.mople.domain.dm.service;

import com.sprint.mople.domain.dm.event.MessageCreatedEvent;
import com.sprint.mople.domain.dm.exception.ChatRoomNotFoundException;
import com.sprint.mople.domain.dm.dto.ChatRoomResponse;
import com.sprint.mople.domain.dm.dto.MessageCreateRequest;
import com.sprint.mople.domain.dm.dto.MessageResponse;
import com.sprint.mople.domain.dm.entity.ChatRoom;
import com.sprint.mople.domain.dm.entity.Message;
import com.sprint.mople.domain.dm.mapper.MessageMapper;
import com.sprint.mople.domain.dm.repository.ChatRoomRepository;
import com.sprint.mople.domain.dm.repository.ChatRoomUserRepository;
import com.sprint.mople.domain.dm.repository.MessageRepository;
import com.sprint.mople.domain.notification.entity.NotificationType;
import com.sprint.mople.domain.notification.service.NotificationService;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
  private final ApplicationEventPublisher eventPublisher;
  private final NotificationService notificationService;

  @Transactional
  @Override
  public MessageResponse create(MessageCreateRequest request) {
    log.debug("메세지 생성 시작 - 보낸이: {}, 받는이: {}, 내용: {}", request.senderId(), request.receiverId(), request.content());
    User sender = userRepository.findById(request.senderId())
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    User receiver = userRepository.findById(request.receiverId())
        .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
    ChatRoom chatRoom = chatRoomUserRepository.findChatRoomByUserIds(request.senderId(), request.receiverId())
        .orElseGet(()->  {
          ChatRoomResponse chatRoomResponse = chatRoomService.createChatRoom(request.senderId(), request.receiverId());
          return chatRoomRepository.findById(chatRoomResponse.id())
              .orElseThrow(ChatRoomNotFoundException::new);
        });
    Message message = new Message(chatRoom, sender, request.content());
    messageRepository.save(message);
    eventPublisher.publishEvent(new MessageCreatedEvent(messageMapper.toDto(message)));
    log.debug("메세지 생성 완료 - 메세지 ID: {}", message.getId());

    String content = String.format("%s님이 새로운 메세지를 보냈습니다.", sender.getUserName());
    notificationService.send(receiver, NotificationType.DM_RECEIVED, content, null);
    log.debug("메세지 알림 전송 완료 - 대상: {}", receiver.getId());
    return messageMapper.toDto(message);
  }

  @Transactional(readOnly = true)
  @Override
  public List<MessageResponse> findAll(UUID requestUserId, UUID targetUserId) {
    log.debug("DM 전체 메세지 조회 시작 - 요청 유저: {}, 대상 유저: {}", requestUserId, targetUserId);
    ChatRoom chatRoom = chatRoomUserRepository.findChatRoomByUserIds(requestUserId, targetUserId)
        .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
    UUID chatRoomId = chatRoom.getId();
    List<Message> messages = messageRepository.findAllByChatRoomId(chatRoomId);
    return messages.stream().map(messageMapper::toDto).toList();
  }
}
