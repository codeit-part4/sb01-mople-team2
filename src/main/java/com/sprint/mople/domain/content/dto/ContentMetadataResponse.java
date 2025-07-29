package com.sprint.mople.domain.content.dto;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import com.sprint.mople.domain.content.entity.Content.Source;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;

@Builder
public record ContentMetadataResponse(

    @Schema(description = "콘텐츠 UUID", example = "123e4567-e89b-12d3-a456-426614174000")
    UUID id,

    @Schema(description = "외부 API의 콘텐츠 ID", example = "tmdb_43923")
    String externalId,

    @Schema(description = "데이터 소스", example = "TMDB")
    Source source,

    @Schema(description = "콘텐츠 제목", example = "인터스텔라")
    String title,

    @Schema(description = "콘텐츠 요약 설명")
    String summary,

    @Schema(description = "카테고리", example = "MOVIE")
    Category category,

    @Schema(description = "포스터 URL")
    String posterUrl,

    @Schema(description = "장르 리스트", example = "[\"SF\", \"Drama\"]")
    Set<String> genres,

    @Schema(description = "개봉일")
    Instant releasedAt,

    @Schema(description = "콘텐츠 리뷰 개수")
    Long totalRatingCount,

    @Schema(description = "평점")
    BigDecimal averageRating
) {

    public static ContentMetadataResponse from(Content content) {
        return ContentMetadataResponse.builder()
            .id(content.getId())
            .externalId(content.getExternalId())
            .source(content.getSource())
            .title(content.getTitle())
            .summary(content.getSummary())
            .category(content.getCategory())
            .posterUrl(content.getPosterUrl())
            .genres(content.getGenres())
            .releasedAt(content.getReleasedAt())
            .totalRatingCount(content.getTotalRatingCount())
            .averageRating(content.getAverageRating())
            .build();
    }
}