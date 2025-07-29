package com.sprint.mople.domain.playlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PlaylistCategoryRequest(
    @Schema(description = "플레이리스트 카테고리 제목", example = "비")
    @NotBlank(message = "제목은 필수 입력값입니다.")
    String name
) {
}
