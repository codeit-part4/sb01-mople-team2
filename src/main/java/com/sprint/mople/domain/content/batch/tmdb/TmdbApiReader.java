package com.sprint.mople.domain.content.batch.tmdb;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class TmdbApiReader implements ItemStreamReader<TmdbItemDto> {

  private final RestTemplate restTemplate;
  private final String baseUrl;
  private final String apiToken;

  private List<TmdbApiRequestInfo> requestInfos;
  private int requestIndex = 0;

  private List<TmdbItemDto> currentItems = new ArrayList<>();
  private int itemIndex = 0;
  private boolean initialized = false;

  public TmdbApiReader(RestTemplate restTemplate, String baseUrl, String apiToken) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
    this.apiToken = apiToken;
  }

  @Override
  public TmdbItemDto read() throws Exception {
    if (!initialized) {
      this.requestInfos = generateRequestInfos();
      this.initialized = true;
    }

    while (true) {
      if (itemIndex >= currentItems.size()) {
        if (!loadNextItems()) {
          return null;
        }
      }
      if (itemIndex < currentItems.size()) {
        return currentItems.get(itemIndex++);
      }
    }
  }

  private List<TmdbApiRequestInfo> generateRequestInfos() {
    List<TmdbApiRequestInfo> infos = new ArrayList<>();
    List<String> types = List.of("movie", "tv");

    for (String type : types) {
      TmdbApiResponse response = callFirstPage(type);
      if (response == null) continue;

      int totalPages = Integer.parseInt(response.getTotalPages());
      for (int page = 1; page <= totalPages; page++) {
        infos.add(TmdbApiRequestInfo.builder()
            .videoType(type)
            .page(String.valueOf(page))
            .build());
      }
    }

    log.info("[TMDB API 요청 초기화] 총 요청 수: {}", infos.size());
    return infos;
  }

  private boolean loadNextItems() {
    if (requestIndex >= requestInfos.size()) return false;

    TmdbApiRequestInfo info = requestInfos.get(requestIndex++);
    URI uri = buildUri(info.getVideoType(), Integer.parseInt(info.getPage()));

    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    headers.set("Authorization", "Bearer " + apiToken);

    try {
      ResponseEntity<TmdbApiResponse> response = restTemplate.exchange(
          uri,
          HttpMethod.GET,
          new HttpEntity<>(headers),
          TmdbApiResponse.class
      );

      if (response.getBody() != null && response.getBody().getResults() != null) {
        currentItems = response.getBody().getResults();
      } else {
        currentItems = new ArrayList<>();
      }
      itemIndex = 0;

      log.debug("✅ TMDB 데이터 패칭 성공 - videoType={}, page={}, itemCount={}",
          info.getVideoType(), info.getPage(), currentItems.size());

      return true;

    } catch (Exception e) {
      log.warn("❌ TMDB API 호출 실패 - videoType={}, page={}", info.getVideoType(), info.getPage(), e);
      return true;
    }
  }

  private TmdbApiResponse callFirstPage(String videoType) {
    URI uri = buildUri(videoType, 1);

    HttpHeaders headers = new HttpHeaders();
    headers.set("accept", "application/json");
    headers.set("Authorization", "Bearer " + apiToken);

    try {
      ResponseEntity<TmdbApiResponse> response = restTemplate.exchange(
          uri,
          HttpMethod.GET,
          new HttpEntity<>(headers),
          TmdbApiResponse.class
      );

      return response.getBody();

    } catch (Exception e) {
      log.warn("❌ 첫 페이지 호출 실패 - videoType={}", videoType, e);
      return null;
    }
  }

  private URI buildUri(String type, int page) {
    UriComponentsBuilder builder;

    if ("movie".equals(type)) {
      builder = UriComponentsBuilder
          .fromUriString(baseUrl)
          .path("/movie/now_playing")
          .queryParam("language", "ko-KR")
          .queryParam("page", page)
          .queryParam("region", "KR");
    } else if ("tv".equals(type)) {
      LocalDate today = LocalDate.now();
      LocalDate weekAgo = today.minusDays(7);

      builder = UriComponentsBuilder
          .fromUriString(baseUrl)
          .path("/discover/tv")
          .queryParam("air_date.gte", weekAgo)
          .queryParam("air_date.lte", today)
          .queryParam("include_adult", "false")
          .queryParam("include_null_first_air_dates", "false")
          .queryParam("language", "ko-KR")
          .queryParam("page", page)
          .queryParam("sort_by", "popularity.desc")
          .queryParam("with_origin_country", "KR");
    } else {
      throw new IllegalArgumentException("Unsupported video type: " + type);
    }

    return builder.build().toUri();
  }

  @Override
  public void open(ExecutionContext context) throws ItemStreamException {
    this.requestIndex = context.containsKey("requestIndex") ? context.getInt("requestIndex") : 0;
    this.itemIndex = context.containsKey("itemIndex") ? context.getInt("itemIndex") : 0;
    log.info("[TmdbApiReader] 리더 시작 - 요청 인덱스: {}, 아이템 인덱스: {}", requestIndex, itemIndex);
  }

  @Override
  public void update(ExecutionContext context) throws ItemStreamException {
    context.putInt("requestIndex", requestIndex);
    context.putInt("itemIndex", itemIndex);
  }

  @Override
  public void close() throws ItemStreamException {
    log.info("[TmdbApiReader] 리더 종료 - 리소스 정리 완료");
  }
}
