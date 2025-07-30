package com.sprint.mople.domain.content.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "컨텐츠 검색 요청 파라미터")
public class ContentSearchRequest {

  @Schema(description = "제목 키워드", example = "인터스텔라")
  private String title;

  @Schema(description = "커서 기반 페이징을 위한 Base64 문자열")
  @Pattern(regexp = "^[A-Za-z0-9-_]+={0,2}$", message = "커서는 Base64 형식이어야 합니다.")
  private String cursor;

  @Schema(description = "한 페이지에서 불러올 콘텐츠 수", example = "30", defaultValue = "30")
  @Min(value = 1, message = "최소 1개 이상 조회해야 합니다.")
  @Max(value = 50, message = "최대 50개까지 조회할 수 있습니다.")
  private int size = 30;
}
