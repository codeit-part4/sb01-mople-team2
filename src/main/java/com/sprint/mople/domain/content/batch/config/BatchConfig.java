package com.sprint.mople.domain.content.batch.config;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final ContentRepository contentRepository;

  @Bean
  public ItemWriter<Content> contentApiWriter() {
    return new ContentApiWriter(contentRepository);
  }

}
