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

@Component
@Slf4j
public class SportsJobScheduler {

  private final JobLauncher jobLauncher;
  private final Job sportsJob;

  public SportsJobScheduler(
      JobLauncher jobLauncher,
      @Qualifier("sportsContentJob") Job sportsJob
  ) {
    this.jobLauncher = jobLauncher;
    this.sportsJob = sportsJob;
  }

  /**
   * 매일 오전 4시 30분에 스포츠 콘텐츠 배치 작업 실행
   */
  @Scheduled(cron = "0 30 4 * * *")
  public void runSportsJob() {
    JobParameters jobParameters = new JobParametersBuilder()
        .addLong("timestamp", System.currentTimeMillis())
        .addString("mode", "scheduler")
        .toJobParameters();

    try {
      log.info("📅 스포츠 배치 작업 스케줄 시작");
      JobExecution jobExecution = jobLauncher.run(sportsJob, jobParameters);
      log.info("✅ 스포츠 배치 작업 완료: {}", jobExecution.getStatus());

    } catch (Exception e) {
      log.error("❌ 스포츠 배치 작업 실행 중 오류 발생", e);
    }
  }
}
