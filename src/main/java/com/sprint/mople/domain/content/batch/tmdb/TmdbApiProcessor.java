package com.sprint.mople.domain.content.batch.tmdb;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.Content.Category;
import com.sprint.mople.domain.content.entity.Content.Source;
import com.sprint.mople.domain.content.entity.Genre;
import com.sprint.mople.domain.content.repository.ContentRepository;
import com.sprint.mople.domain.content.repository.GenreRepository;
import com.sprint.mople.global.util.TitleNormalizer;
import jakarta.annotation.PostConstruct;
import java.net.URI;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * TMDB API 데이터를 Content 도메인으로 변환하는 프로세서
 */
@Slf4j
@RequiredArgsConstructor
public class TmdbApiProcessor implements ItemProcessor<TmdbItemDto, Content> {

  private final ContentRepository contentRepository;
  private final GenreRepository genreRepository;
  private final RestTemplate restTemplate;
  private final String baseUrl;
  private final String apiToken;

  @PostConstruct
  @Transactional
  public void updateGenres() {
    log.info("🎬 TMDB 장르 정보 업데이트 배치 시작...");

    Map<Long, String> movieGenres = fetchGenreMap("movie");
    Map<Long, String> tvGenres = fetchGenreMap("tv");

    Set<String> allGenreNames = new HashSet<>();
    allGenreNames.addAll(movieGenres.values());
    allGenreNames.addAll(tvGenres.values());

    List<Genre> existingGenres = genreRepository.findAll();
    Set<String> existingGenreNames = existingGenres.stream()
        .map(Genre::getName)
        .collect(Collectors.toSet());

    Set<String> newGenreNames = allGenreNames.stream()
        .filter(name -> !existingGenreNames.contains(name))
        .collect(Collectors.toSet());

    List<Genre> newGenres = newGenreNames.stream()
        .map(Genre::new)
        .collect(Collectors.toList());

    if (!newGenres.isEmpty()) {
      genreRepository.saveAll(newGenres);
    }

    log.info("✅ TMDB 장르 업데이트 배치 완료 - 총 {}개 장르", genreRepository.count());
  }

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

    Set<Genre> genres = new HashSet<>();

    if (item.getGenreIds() != null && !item.getGenreIds().isEmpty()) {
      genres = new HashSet<>(genreRepository.findAllById(item.getGenreIds()));
    }

    // 4. 제목 정규화
    String normalizedTitle = TitleNormalizer.normalize(rawTitle);

    // 5. Content 객체 생성
    Content content = Content.builder()
        .externalId(item.getId())
        .source(Source.TMDB)
        .title(rawTitle)
        .normalizedTitle(normalizedTitle)
        .summary(item.getOverview())
        .category(category)
        .posterUrl(posterUrl)
        .releasedAt(releasedAt)
        .genres(genres)
        .build();

    log.info("✅ [TmdbProcessor] 컨텐츠 변환 성공 - title: {}", normalizedTitle);
    return content;
  }

  private Map<Long, String> fetchGenreMap(String type) {
    Map<Long, String> map = new HashMap<>();

    URI uri = UriComponentsBuilder
        .fromUriString(baseUrl)
        .path("/genre/" + type + "/list")
        .queryParam("language", "ko")
        .build().toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    headers.set("Authorization", "Bearer " + apiToken);

    try {
      ResponseEntity<TmdbGenreResponse> response = restTemplate.exchange(
          uri,
          HttpMethod.GET,
          new HttpEntity<>(headers),
          TmdbGenreResponse.class
      );

      if (response.getBody() != null) {
        for (TmdbGenreDto genre : response.getBody().getGenres()) {
          map.put(Long.valueOf(genre.getId()), genre.getName());
        }
      }
    } catch (Exception e) {
      log.warn("❌ 장르 목록 호출 실패 - 타입: {}", type, e);
    }

    return map;
  }
}
