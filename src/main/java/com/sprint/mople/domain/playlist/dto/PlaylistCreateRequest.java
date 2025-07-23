package com.sprint.mople.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 플레이리스트 생성 요청 DTO
 *
 * @param title       플레이리스트 제목
 * @param description 플레이리스트 설명
 * @param isPublic    플레이리스트 공개여부
 */
@Schema(description = "플레이리스트 생성 요청 DTO")
public record PlaylistCreateRequest(

    @Schema(description = "플레이리스트 제목", example = "감동적인 영화 모음")
    @NotBlank(message = "제목은 필수 입력값입니다.")
    String title,

    @Schema(description = "플레이리스트 설명", example = "휴지 없이는 볼 수 없는 영화들입니다.")
    @NotBlank(message = "설명은 필수 입력값입니다.")
    String description,

    @Schema(description = "공개 여부", example = "true")
    @NotNull(message = "공개 여부는 필수 입력값입니다.")
    Boolean isPublic

) {
}
