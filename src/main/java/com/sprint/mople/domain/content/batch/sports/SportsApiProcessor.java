package com.sprint.mople.domain.content.batch.sports;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.global.util.TitleNormalizer;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * 스포츠 API 데이터를 Content 도메인으로 변환하는 프로세서
 */
@Slf4j
@RequiredArgsConstructor
public class SportsApiProcessor implements ItemProcessor<SportsItemDto, Content> {

  private final ContentRepository contentRepository;

  @Override
  public Content process(SportsItemDto item) throws Exception {

    // 제목 정규화
    String normalizedTitle = TitleNormalizer.normalize(item.getFilename());

    log.info("🎽 [SportsProcessor] 처리 시작 - title: {}", normalizedTitle);

    if (normalizedTitle.isBlank() || contentRepository.existsByTitle(normalizedTitle)) {
      log.debug("⛔ 중복 혹은 빈 제목으로 인해 스킵: {}", normalizedTitle);
      return null;
    }

    // 설명 생성
    StringBuilder description = new StringBuilder();
    if (item.getLeagueName() != null) description.append("리그: ").append(item.getLeagueName()).append("\n");
    if (item.getVenue() != null) description.append("장소: ").append(item.getVenue()).append("\n\n");
    if (item.getHomeTeam() != null && item.getAwayTeam() != null)
      description.append(item.getHomeTeam()).append(" vs ").append(item.getAwayTeam()).append("\n");
    if (item.getHomeScore() != null && item.getAwayScore() != null)
      description.append(item.getHomeScore()).append(":").append(item.getAwayScore());

    log.debug("📄 설명 생성 완료: {}", description.length() > 40 ? description.substring(0, 40) + "..." : description);

    // 날짜 및 시간 변환
    Instant releasedAt = null;
    try {
      String date = item.getEventDate();
      String time = item.getUtcTime();
      if (date != null && !date.isEmpty() && time != null && !time.isEmpty()) {
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);

        ZonedDateTime utcDateTime = ZonedDateTime.of(localDate, localTime, ZoneId.of("UTC"));
        releasedAt = utcDateTime.toInstant();

        log.debug("⏰ 날짜 변환 완료: date={}, time={}, releasedAt={}", date, time, releasedAt);
      }
    } catch (Exception e) {
      log.warn("⚠️ 날짜 파싱 실패: date={}, time={}", item.getEventDate(), item.getUtcTime());
    }

    // 영상 확인
    String youtubeUrl = item.getVideoUrl();
    if (youtubeUrl == null || youtubeUrl.isBlank()) {
      log.debug("🎬 유튜브 영상 없음 - 스킵: {}", normalizedTitle);
      return null;
    }


    // Content 객체 생성
    Content content = Content.builder()
        .externalId(null) // You can pass null or a meaningful value if available
        .source(Content.Source.THE_SPORTS_DB)
        .title(normalizedTitle)
        .summary(description.toString())
        .category(Content.Category.SPORTS)
        .posterUrl(item.getThumbnailUrl())
        .releasedAt(releasedAt)
        .averageRating(BigDecimal.ZERO)
        .totalRatingCount(0L)
        .build();

    log.info("✅ [SportsProcessor] 컨텐츠 변환 성공 - title: {}", normalizedTitle);
    return content;
  }
}
