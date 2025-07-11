package com.sprint.mople.domain.content.dto;


import com.sprint.mople.domain.content.entity.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ContentCardResponse(
    @Schema(description = "콘텐츠 ID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "콘텐츠 제목", example = "제목")
    String title,

    @Schema(description = "콘텐츠 좋아요 수", example = "42")
    long likeCount,

    @Schema(description = "콘텐츠 생성 시간")
    Instant createdAt,

    @Schema(description = "콘텐츠 수정 시간")
    Instant updatedAt,

    @Schema(description = "콘텐츠 포스터 url")
    String posterUrl,

    @Schema(description = "콘텐츠 리뷰 개수")
    Long totalRatingCount,

    @Schema(description = "콘텐츠 평균 평점")
    BigDecimal averageRating,

    @Schema(description = "사용자의 좋아요 여부", example = "true")
    boolean liked
)
{

    public static ContentCardResponse from(Content content) {
        return ContentCardResponse
            .builder()
            .id(content.getId())
            .title(content.getTitle())
            .likeCount(content.getContentLikes().size())
            .createdAt(content.getCreatedAt())
            .updatedAt(content.getUpdatedAt())
            .posterUrl(content.getPosterUrl())
            .totalRatingCount(content.getTotalRatingCount())
            .averageRating(content.getAverageRating())
            .liked(false)
            .build();
    }
}
