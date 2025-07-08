package com.sprint.mople.domain.content.api;

import com.sprint.mople.domain.content.dto.ContentResponse;
import com.sprint.mople.domain.content.entity.ContentSortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Content Recommend", description = "콘텐츠 추천 API")
@RequestMapping("/api/contents/recommend")
public interface ContentRecommendApi {

  @Operation(
      summary = "추천 콘텐츠 조회",
      description = "가중치 점수 기반으로 상위 N개의 추천 콘텐츠를 반환합니다."
  )
  @GetMapping
  ResponseEntity<List<ContentResponse>> getRecommendedContents(
      @Parameter(description = "가져올 콘텐츠 개수", example = "20")
      @RequestParam(defaultValue = "20") int limit
  );

  @Operation(summary = "정렬 조건별 콘텐츠 조회", description = "최신순, 리뷰많은순, 점수순 정렬로 콘텐츠를 조회합니다.")
  @GetMapping("/sorted")
  ResponseEntity<List<ContentResponse>> getSortedContents(
      @RequestParam(defaultValue = "20") int limit,
      @RequestParam(defaultValue = "SCORE") ContentSortType sortType
  );
}
