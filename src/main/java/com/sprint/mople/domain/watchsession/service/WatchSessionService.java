package com.sprint.mople.domain.watchsession.service;

import com.sprint.mople.domain.watchsession.dto.WatchSessionCreateRequest;
import com.sprint.mople.domain.watchsession.dto.WatchSessionParticipantResponse;
import com.sprint.mople.domain.watchsession.dto.WatchSessionResponse;
import java.util.List;
import java.util.UUID;

public interface WatchSessionService {
  WatchSessionResponse createWatchSession(WatchSessionCreateRequest request);

  WatchSessionResponse getWatchSessionById(UUID sessionId);

  WatchSessionResponse getWatchSessionByContentId(UUID contentId);

  void joinSession(UUID sessionId, UUID userId);

  void leaveSession(UUID sessionId, UUID userId);

  List<WatchSessionParticipantResponse> getParticipants(UUID sessionId, String usernameFilter);
}
