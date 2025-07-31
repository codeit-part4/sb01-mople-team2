package com.sprint.mople.domain.content.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

  private final JobRepository jobRepository;
  private final Step tmdbContentStep;
  private final Step sportsContentStep;

  @Bean
  public Job tmdbContentJob() {
    return new JobBuilder("tmdbContentJob", jobRepository)
        .start(tmdbContentStep)
        .build();
  }

  @Bean
  public Job sportsContentJob() {
    return new JobBuilder("sportsContentJob", jobRepository)
        .start(sportsContentStep)
        .build();
  }
}
