package com.sprint.mople.domain.content.dto;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import com.sprint.mople.domain.content.entity.Content.Source;
import com.sprint.mople.domain.content.entity.Genre;
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
    String description,

    @Schema(description = "카테고리", example = "MOVIE")
    Category category,

    @Schema(description = "포스터 URL")
    String posterUrl,

    @Schema(description = "장르 리스트", example = "[\"SF\", \"Drama\"]")
    Set<Genre> genres,

    @Schema(description = "개봉일")
    Instant releasedAt,

    @Schema(description = "콘텐츠 리뷰 개수")
    Long reviews,

    @Schema(description = "평점")
    BigDecimal rating,

    @Schema(description = "현재 컨텐츠를 보고 있는 시청자 수")
    int viewers
) {

    public static ContentMetadataResponse from(Content content, int viewers) {
        return ContentMetadataResponse.builder()
            .id(content.getId())
            .externalId(content.getExternalId())
            .source(content.getSource())
            .title(content.getTitle())
            .description(content.getSummary())
            .category(content.getCategory())
            .posterUrl(content.getPosterUrl())
            .genres(content.getGenres())
            .releasedAt(content.getReleasedAt())
            .reviews(content.getTotalRatingCount())
            .rating(content.getAverageRating())
            .viewers(viewers)
            .build();
    }
}
