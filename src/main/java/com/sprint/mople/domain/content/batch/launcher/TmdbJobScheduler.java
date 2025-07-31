package com.sprint.mople.domain.content.batch.launcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TmdbJobScheduler {

  private final JobLauncher jobLauncher;
  private final Job tmdbJob;

  public TmdbJobScheduler(
      JobLauncher jobLauncher,
      @Qualifier("tmdbContentJob") Job tmdbJob
  ) {
    this.jobLauncher = jobLauncher;
    this.tmdbJob = tmdbJob;
  }

  /**
   * 매주 월요일 오전 7시에 TMDB 콘텐츠 배치 작업 실행
   */
  @Scheduled(cron = "0 0 7 ? * MON")
  public void runTmdbJob() {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("timestamp", System.currentTimeMillis())
        .addString("mode", "scheduler")
        .toJobParameters();

    try {
      log.info("📅 TMDB 배치 작업 스케줄 시작");
      JobExecution jobExecution = jobLauncher.run(tmdbJob, jobParameters);
      log.info("✅ TMDB 배치 작업 완료: {}", jobExecution.getStatus());

    } catch (Exception e) {
      log.error("❌ TMDB 배치 작업 실행 중 오류 발생", e);
    }
  }
}
