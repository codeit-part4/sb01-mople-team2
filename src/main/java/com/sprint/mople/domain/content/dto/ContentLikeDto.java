package com.sprint.mople.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class ContentLikeDto {

  public record LikeStatusResponse(
      @Schema(description = "좋아요 여부", example = "true") boolean liked
  ) {}

  public record LikeCountResponse(
      @Schema(
          description = "콘텐츠 ID",
          example = "123e4567-e89b-12d3-a456-426614174000"
      ) UUID contentId,
      @Schema(description = "좋아요 수", example = "42") long count
  ) {}
}
