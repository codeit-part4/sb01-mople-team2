package com.sprint.mople.domain.content.batch.tmdb;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class TmdbBatchConfig {

  private final ContentRepository contentRepository;
  private final RestTemplate restTemplate;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ItemWriter<Content> contentApiWriter;

  @Value("${tmdb.baseurl}")
  private String baseUrl;

  @Value("${tmdb.api_token}")
  private String apiToken;

  @Bean
  public Step tmdbContentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("tmdbContentStep", jobRepository)
        .<TmdbItemDto, Content>chunk(20, transactionManager)
        .reader(tmdbReader())
        .processor(tmdbProcessor())
        .writer(contentApiWriter)
        .build();
  }

  @Bean
  public ItemStreamReader<TmdbItemDto> tmdbReader() {
    return new TmdbApiReader(restTemplate, baseUrl, apiToken);
  }

  @Bean
  public ItemProcessor<TmdbItemDto, Content> tmdbProcessor() {
    return new TmdbApiProcessor(contentRepository, restTemplate, baseUrl, apiToken);
  }
}
