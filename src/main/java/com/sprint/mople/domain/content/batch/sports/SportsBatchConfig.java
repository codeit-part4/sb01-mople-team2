package com.sprint.mople.domain.content.batch.sports;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class SportsBatchConfig {

  private final ContentRepository contentRepository;
  private final RestTemplate restTemplate;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final ItemWriter<Content> contentApiWriter;

  @Value("${sports.baseurl}")
  private String baseUrl;

  @Bean
  public Step sportsContentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("sportsContentStep", jobRepository)
        .<SportsItemDto, Content>chunk(20, transactionManager)
        .reader(sportsReader())
        .processor(sportsProcessor())
        .writer(contentApiWriter)
        .build();
  }

  @Bean
  public ItemReader<SportsItemDto> sportsReader() {
    return new SportsApiReader(restTemplate, baseUrl);
  }

  @Bean
  public ItemProcessor<SportsItemDto, Content> sportsProcessor() {
    return new SportsApiProcessor(contentRepository);
  }

}
