package com.sprint.mople.domain.content.controller;

import com.sprint.mople.domain.content.api.ContentRecommendApi;
import com.sprint.mople.domain.content.dto.ContentCardResponse;
import com.sprint.mople.domain.content.entity.ContentSortType;
import com.sprint.mople.domain.content.service.ContentRecommendService;
import com.sprint.mople.global.jwt.JwtProvider;
import com.sprint.mople.global.jwt.JwtTokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContentRecommendController implements ContentRecommendApi {

  private final ContentRecommendService contentRecommendService;
  private final HttpServletRequest request;
  private final JwtProvider jwtProvider;

  // TODO SecurityContext 대체
  private UUID getRequestUserId() {
    String token = JwtTokenExtractor.resolveToken(request);
    return jwtProvider.getUserId(token);
  }

  @Override
  public ResponseEntity<List<ContentCardResponse>> getRecommendedContents(int limit) {
    List<ContentCardResponse> responses =
        contentRecommendService.getRecommendedContents(getRequestUserId(), limit);
    return ResponseEntity.ok(responses);
  }

  @Override
  public ResponseEntity<List<ContentCardResponse>> getSortedContents(
      int limit,
      ContentSortType sortType
  )
  {
    return ResponseEntity.ok(
        contentRecommendService.getSortedContents(
            getRequestUserId(),
            limit,
            sortType
        ));
  }

}
