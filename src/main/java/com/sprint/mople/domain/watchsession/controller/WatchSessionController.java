package com.sprint.mople.domain.watchsession.controller;

import com.sprint.mople.domain.watchsession.dto.WatchSessionParticipantResponse;
import com.sprint.mople.domain.watchsession.dto.WatchSessionResponse;
import com.sprint.mople.domain.watchsession.service.WatchSessionService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/watch-sessions")
@RequiredArgsConstructor
public class WatchSessionController {

  private final WatchSessionService watchSessionService;

  @GetMapping("/content/{contentId}")
  public ResponseEntity<WatchSessionResponse> getWatchSessionByContent(@PathVariable UUID contentId) {
    return ResponseEntity.ok(watchSessionService.getWatchSessionByContentId(contentId));
  }

//  @GetMapping("/{sessionId}/participants")
//  public ResponseEntity<List<WatchSessionParticipantResponse>> getParticipants(@PathVariable UUID sessionId) {
//    return ResponseEntity.ok(watchSessionService.getParticipantsBySessionId(sessionId));
//  }
}
