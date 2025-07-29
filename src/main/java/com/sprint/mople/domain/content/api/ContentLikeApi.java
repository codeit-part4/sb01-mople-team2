package com.sprint.mople.domain.content.api;

import com.sprint.mople.domain.content.dto.ContentLikeDto.LikeCountResponse;
import com.sprint.mople.domain.content.dto.ContentLikeDto.LikeStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Content Like", description = "콘텐츠 좋아요 관련 API")
@RequestMapping("/api/contents/{contentId}/likes")
public interface ContentLikeApi {

  @Operation(
      summary = "콘텐츠 좋아요 등록",
      description = "지정한 콘텐츠에 대해 로그인한 사용자가 좋아요를 누릅니다."
  )
  @PostMapping
  ResponseEntity<Void> like(
      @Parameter(description = "콘텐츠 ID", required = true)
      @PathVariable UUID contentId, HttpServletRequest request
  );

  @Operation(
      summary = "콘텐츠 좋아요 취소",
      description = "지정한 콘텐츠에 대해 로그인한 사용자가 좋아요를 취소합니다."
  )
  @DeleteMapping
  ResponseEntity<Void> unlike(
      @Parameter(description = "콘텐츠 ID", required = true)
      @PathVariable UUID contentId, HttpServletRequest request
  );

  @Operation(
      summary = "사용자의 콘텐츠 좋아요 여부 확인",
      description = "현재 로그인한 사용자가 해당 콘텐츠를 좋아요했는지 확인합니다."
  )
  @GetMapping("/me")
  ResponseEntity<LikeStatusResponse> isLiked(
      @Parameter(description = "콘텐츠 ID", required = true)
      @PathVariable UUID contentId, HttpServletRequest request
  );

  @Operation(
      summary = "콘텐츠 좋아요 수 조회",
      description = "특정 콘텐츠에 대한 전체 좋아요 수를 반환합니다."
  )
  @GetMapping
  ResponseEntity<LikeCountResponse> likeCount(
      @Parameter(description = "콘텐츠 ID", required = true)
      @PathVariable UUID contentId
  );
}
