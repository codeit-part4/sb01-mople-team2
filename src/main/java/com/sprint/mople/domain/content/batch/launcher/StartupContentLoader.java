package com.sprint.mople.domain.content.batch.launcher;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupContentLoader implements ApplicationRunner {

  private final JobLauncher jobLauncher;
  private final Job tmdbContentJob;
  private final Job sportsContentJob;
  private final ContentRepository contentRepository;

  @Override
  public void run(ApplicationArguments args) {
    try {
      if (!contentRepository.existsByCategory(Content.Category.MOVIE)
          && !contentRepository.existsByCategory(Content.Category.TV)) {

        log.info("🎬 초기 TMDB 콘텐츠 불러오기 시작...");
        JobExecution tmdbJobExecution = jobLauncher.run(
            tmdbContentJob,
            new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("mode", "initial")
                .toJobParameters()
        );
        log.info("✅ TMDB 배치 작업 완료: 상태={}", tmdbJobExecution.getStatus());
      }

      if (!contentRepository.existsByCategory(Content.Category.SPORTS)) {
        log.info("🏈 초기 스포츠 콘텐츠 불러오기 시작...");
        JobExecution sportsJobExecution = jobLauncher.run(
            sportsContentJob,
            new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("mode", "initial")
                .toJobParameters()
        );
        log.info("✅ 스포츠 배치 작업 완료: 상태={}", sportsJobExecution.getStatus());
      }

    } catch (Exception e) {
      log.error("❌ 애플리케이션 시작 시 콘텐츠 불러오기 실패", e);
    }
  }
}