package com.sprint.mople.domain.watchsession.service;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.exception.ContentNotFoundException;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.domain.user.exception.NotFoundException;
import com.sprint.mople.domain.user.repository.UserRepository;
import com.sprint.mople.domain.watchsession.dto.WatchSessionCreateRequest;
import com.sprint.mople.domain.watchsession.dto.WatchSessionParticipantResponse;
import com.sprint.mople.domain.watchsession.dto.WatchSessionResponse;
import com.sprint.mople.domain.watchsession.entity.WatchSession;
import com.sprint.mople.domain.watchsession.entity.WatchSessionParticipant;
import com.sprint.mople.domain.watchsession.exception.SessionAlreadyJoinedException;
import com.sprint.mople.domain.watchsession.exception.WatchSessionNotFoundException;
import com.sprint.mople.domain.watchsession.exception.ParticipantNotFoundException;
import com.sprint.mople.domain.watchsession.repository.WatchSessionParticipantRepository;
import com.sprint.mople.domain.watchsession.repository.WatchSessionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class WatchSessionServiceImpl implements WatchSessionService {

  private final WatchSessionRepository watchSessionRepository;
  private final ContentRepository contentRepository;
  private final WatchSessionParticipantRepository watchSessionParticipantRepository;
  private final UserRepository userRepository;

  @Override
  public WatchSessionResponse createWatchSession(WatchSessionCreateRequest request) {
    Content content = contentRepository.findById(request.contentId())
        .orElseThrow(ContentNotFoundException::new);

    WatchSession session = WatchSession.builder()
        .content(content)
        .build();

    WatchSession saved = watchSessionRepository.save(session);

    int participantCount = watchSessionParticipantRepository.countBySessionId(saved.getId());
    return WatchSessionResponse.from(saved, participantCount);
  }

  @Override
  public WatchSessionResponse getWatchSessionById(UUID sessionId) {
    WatchSession session = watchSessionRepository.findById(sessionId)
        .orElseThrow(WatchSessionNotFoundException::new);

    int participantCount = watchSessionParticipantRepository.countBySessionId(sessionId);
    return WatchSessionResponse.from(session, participantCount);
  }

  @Override
  public WatchSessionResponse getWatchSessionByContentId(UUID contentId) {
    return watchSessionRepository.findByContentId(contentId)
        .map(session -> {
          int participantCount = watchSessionParticipantRepository.countBySessionId(session.getId());
          return WatchSessionResponse.from(session, participantCount);
        })
        .orElseGet(() -> createWatchSession(new WatchSessionCreateRequest(contentId)));
  }

  @Transactional
  @Override
  public void joinSession(UUID sessionId, UUID userId) {
    WatchSession session = watchSessionRepository.findById(sessionId)
        .orElseThrow(WatchSessionNotFoundException::new);

    watchSessionParticipantRepository.findByUserId(userId)
        .ifPresent(watchSessionParticipantRepository::delete);

    boolean alreadyJoined = watchSessionParticipantRepository.existsBySessionIdAndUserId(sessionId, userId);
    if (alreadyJoined) {
      throw new SessionAlreadyJoinedException();
    }

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("해당 ID의 유저가 존재하지 않습니다."));

    WatchSessionParticipant participant = WatchSessionParticipant.builder()
        .session(session)
        .user(user)
        .build();

    watchSessionParticipantRepository.save(participant);
  }

  @Transactional
  @Override
  public void leaveSession(UUID sessionId, UUID userId) {
    WatchSessionParticipant participant = watchSessionParticipantRepository.findBySessionIdAndUserId(sessionId, userId)
        .orElseThrow(ParticipantNotFoundException::new);

    watchSessionParticipantRepository.delete(participant);
  }

  @Override
  public List<WatchSessionParticipantResponse> getParticipants(UUID sessionId, String usernameFilter) {
    watchSessionRepository.findById(sessionId)
        .orElseThrow(WatchSessionNotFoundException::new);

    List<WatchSessionParticipant> participants = watchSessionParticipantRepository.findAllBySessionId(sessionId);

    return participants.stream()
        .filter(p -> usernameFilter == null || p.getUser().getUserName().toLowerCase().contains(usernameFilter.toLowerCase()))
        .map(WatchSessionParticipantResponse::from)
        .toList();
  }
}
