package com.sprint.mople.domain.content.controller;

import com.sprint.mople.domain.content.api.ContentLikeApi;
import com.sprint.mople.domain.content.dto.ContentLikeDto.LikeCountResponse;
import com.sprint.mople.domain.content.dto.ContentLikeDto.LikeStatusResponse;
import com.sprint.mople.domain.content.service.ContentLikeService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class ContentLikeController implements ContentLikeApi {

  private final ContentLikeService contentLikeService;
  private final HttpServletRequest request;
  private final JwtProvider jwtProvider;

  // TODO SecurityContext 대체
  private UUID getRequestUserId() {
    String token = JwtTokenExtractor.resolveToken(request);
    return jwtProvider.getUserId(token);
  }

  @Override
  public ResponseEntity<Void> like(UUID contentId) {
    contentLikeService.like(getRequestUserId(), contentId);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> unlike(UUID contentId) {
    contentLikeService.unlike(getRequestUserId(), contentId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<LikeStatusResponse> isLiked(UUID contentId) {
    boolean liked = contentLikeService.hasLiked(getRequestUserId(), contentId);
    return ResponseEntity.ok(new LikeStatusResponse(liked));
  }

  @Override
  public ResponseEntity<LikeCountResponse> likeCount(UUID contentId) {
    long count = contentLikeService.countLikes(contentId);
    return ResponseEntity.ok(new LikeCountResponse(contentId, count));
  }
}
