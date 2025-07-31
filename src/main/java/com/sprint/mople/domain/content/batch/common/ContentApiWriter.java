package com.sprint.mople.domain.content.batch.common;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class ContentApiWriter implements ItemWriter<Content> {

  private final ContentRepository contentRepository;

  @Override
  public void write(Chunk<? extends Content> chunk) throws Exception {
    contentRepository.saveAll(chunk.getItems());
    log.info("총 {}개의 컨텐츠를 저장했습니다.", chunk.getItems().size());
  }
}
