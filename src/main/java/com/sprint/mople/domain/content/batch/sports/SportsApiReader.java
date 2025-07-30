package com.sprint.mople.domain.content.batch.sports;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public class SportsApiReader implements ItemStreamReader<SportsItemDto> {

  private final RestTemplate restTemplate;
  private final String baseUrl;
  private List<SportsApiRequestInfo> requestInfos;
  private int requestIndex = 0;
  private List<SportsItemDto> currentItems;
  private int itemIndex = 0;

  public SportsApiReader(RestTemplate restTemplate, String baseUrl) {
    this.restTemplate = restTemplate;
    this.baseUrl = baseUrl;
    currentItems = new ArrayList<>();
  }

  @Override
  public SportsItemDto read() throws Exception {
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

  private boolean loadNextItems() {
    if (requestIndex >= requestInfos.size()) return false;

    SportsApiRequestInfo info = requestInfos.get(requestIndex++);

    URI uri = UriComponentsBuilder.fromUriString(baseUrl)
        .pathSegment("123", "eventsday.php")
        .queryParam("d", info.getDate())
        .queryParam("l", info.getLeagueName())
        .build().toUri();

    log.info("[SPORTS API 요청] URL: {}", uri);

    SportsApiResponse response = restTemplate.getForObject(uri, SportsApiResponse.class);
    currentItems = (response != null && response.getEvents() != null) ? response.getEvents() : new ArrayList<>();
    itemIndex = 0;

    return true;
  }

  private List<SportsApiRequestInfo> generateRequestInfos() {
    List<SportsApiRequestInfo> infos = new ArrayList<>();
    String date = LocalDate.now().minusDays(1).toString();
    List<String> leagues = List.of("MLB", "Korean KBO League");
    for (String league : leagues) {
      infos.add(SportsApiRequestInfo.builder()
          .date(date)
          .leagueName(league)
          .build());
    }
    return infos;
  }

  @Override
  public void open(ExecutionContext context) throws ItemStreamException {
    this.requestInfos = generateRequestInfos();
    this.requestIndex = context.containsKey("requestIndex") ? context.getInt("requestIndex") : 0;
    this.itemIndex = context.containsKey("itemIndex") ? context.getInt("itemIndex") : 0;
    log.info("[SportsApiReader] 리더 시작 - 요청 인덱스: {}, 아이템 인덱스: {}", requestIndex, itemIndex);
  }

  @Override
  public void update(ExecutionContext context) throws ItemStreamException {
    context.putInt("requestIndex", requestIndex);
    context.putInt("itemIndex", itemIndex);
  }

  @Override
  public void close() throws ItemStreamException {
    log.info("[SportsApiReader] 리더 종료 - 리소스 정리 완료");
  }
}

