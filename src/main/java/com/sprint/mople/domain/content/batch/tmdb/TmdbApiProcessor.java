package com.sprint.mople.domain.content.batch.tmdb;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import com.sprint.mople.domain.content.entity.Content.Source;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.global.util.TitleNormalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.web.client.RestTemplate;

/**
 * TMDB API 데이터를 Content 도메인으로 변환하는 프로세서
 */
@Slf4j
@RequiredArgsConstructor
public class TmdbApiProcessor implements ItemProcessor<TmdbItemDto, Content> {

  private final ContentRepository contentRepository;
  private final RestTemplate restTemplate;
  private final String baseUrl;
  private final String apiToken;

  @Override
  public Content process(TmdbItemDto item) throws Exception {
    log.info("🎬 [TmdbProcessor] 처리 시작 - id: {}", item.getId());

    if (item.getId() == null || contentRepository.existsByExternalId(item.getId())) {
      log.debug("⛔ 중복 혹은 null ID로 인해 스킵: {}", item.getId());
      return null;
    }

    String videoType;
    String rawTitle;
    Category category;
    Instant releasedAt;

    if (item.getFirstAirDate() != null && !item.getFirstAirDate().isEmpty()) {
      videoType = "tv";
      rawTitle = item.getName();
      category = Category.TV;
      releasedAt = LocalDate.parse(item.getFirstAirDate()).atStartOfDay(ZoneOffset.UTC).toInstant();
    } else if (item.getReleaseDate() != null && !item.getReleaseDate().isEmpty()) {
      videoType = "movie";
      rawTitle = item.getTitle();
      category = Category.MOVIE;
      releasedAt = LocalDate.parse(item.getReleaseDate()).atStartOfDay(ZoneOffset.UTC).toInstant();
    } else {
      log.warn("⚠️ 날짜 없음 - 스킵: id={}, title={}", item.getId(), item.getTitle());
      return null;
    }

    String posterUrl = "";
    if (item.getPosterUrl() != null && !item.getPosterUrl().isBlank()) {
      posterUrl = "https://image.tmdb.org/t/p/original" + item.getPosterUrl();
    }

    // 4. 정규화
    String normalizedTitle = TitleNormalizer.normalize(rawTitle);

    // 5. Content 객체 생성
    Content content = Content.builder()
        .externalId(item.getId())
        .source(Source.TMDB)
        .title(normalizedTitle)
        .summary(item.getOverview())
        .category(category)
        .posterUrl(posterUrl)
        .releasedAt(releasedAt)
        .build();

    log.info("✅ [TmdbProcessor] 컨텐츠 변환 성공 - title: {}", normalizedTitle);
    return content;
  }
}
