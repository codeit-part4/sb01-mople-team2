package com.sprint.mople.domain.playlist.controller;

import com.sprint.mople.domain.playlist.dto.SubscriberCountResponse;
import com.sprint.mople.domain.playlist.dto.SubscriptionResponse;
import com.sprint.mople.domain.playlist.dto.SubscriptionStatusResponse;
import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.playlist.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Subscribe", description = "구독 관련 API")
@RestController
@RequestMapping("/api/v1/subscribes")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  @Operation(summary = "플레이리스트 구독하기")
  @ApiResponse(responseCode = "200", description = "구독 성공")
  @PostMapping("/playlists/{playlistId}")
  public ResponseEntity<SubscriptionResponse> subscribePlaylist(
      @PathVariable UUID playlistId
  )
  {
    UUID userId = UUID.randomUUID();
    Subscription subscription = subscriptionService.subscribePlaylist(userId, playlistId);
    return ResponseEntity.ok(SubscriptionResponse.from(subscription));
  }

  @Operation(summary = "플레이리스트 구독 취소")
  @ApiResponse(responseCode = "200", description = "구독 취소 성공")
  @DeleteMapping("/playlists/{playlistId}")
  public ResponseEntity<Void> unsubscribePlaylist(
      @PathVariable UUID playlistId
  )
  {
    UUID userId = UUID.randomUUID();
    subscriptionService.unsubscribePlaylist(userId, playlistId);
    return ResponseEntity
        .ok()
        .build();
  }

  @Operation(summary = "내가 구독한 플레이리스트 목록 조회")
  @GetMapping("/my")
  public ResponseEntity<List<SubscriptionResponse>> getMySubscriptions(
  )
  {
    UUID userId = UUID.randomUUID();
    List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
    List<SubscriptionResponse> response = subscriptions
        .stream()
        .map(SubscriptionResponse::from)
        .toList();
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "플레이리스트의 구독자 수 조회")
  @GetMapping("/playlists/{playlistId}/count")
  public ResponseEntity<SubscriberCountResponse> getPlaylistSubscriberCount(
      @PathVariable UUID playlistId
  )
  {
    SubscriberCountResponse response = subscriptionService.getPlaylistSubscriberCount(playlistId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "플레이리스트 구독 여부 확인")
  @GetMapping("/playlists/{playlistId}/check")
  public ResponseEntity<SubscriptionStatusResponse> checkSubscription(
      @PathVariable UUID playlistId
  )
  {
    UUID userId = UUID.randomUUID();
    boolean isSubscribed = subscriptionService.isSubscribed(userId, playlistId);
    return ResponseEntity.ok(new SubscriptionStatusResponse(isSubscribed));
  }
}
