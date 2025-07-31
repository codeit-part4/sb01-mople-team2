package com.sprint.mople.domain.watchsession.controller;

import com.sprint.mople.domain.watchsession.dto.WatchSessionParticipantResponse;
import com.sprint.mople.domain.watchsession.dto.WatchSessionResponse;
import com.sprint.mople.domain.watchsession.service.WatchSessionService;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @PostMapping("/{sessionId}/join")
  public ResponseEntity<Void> joinSession(
      @PathVariable UUID sessionId,
      Principal principal
  ) {
    if (principal == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UUID userId = UUID.fromString(principal.getName()); // safely parse userId
    watchSessionService.joinSession(sessionId, userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{sessionId}/leave")
  public ResponseEntity<Void> leaveSession(
      @PathVariable UUID sessionId,
      Principal principal
  ) {
    UUID userId = UUID.fromString(principal.getName());
    watchSessionService.leaveSession(sessionId, userId);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{sessionId}/participants")
  public ResponseEntity<List<WatchSessionParticipantResponse>> getParticipants(
      @PathVariable UUID sessionId,
      @RequestParam(required = false) String username
  ) {
    return ResponseEntity.ok(watchSessionService.getParticipants(sessionId, username));
  }
}
