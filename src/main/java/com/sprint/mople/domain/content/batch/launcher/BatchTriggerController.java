package com.sprint.mople.domain.content.batch.launcher;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchTriggerController {

  private final JobLauncher jobLauncher;
  private final Job tmdbContentJob;
  private final Job sportsContentJob;

  public BatchTriggerController(
      JobLauncher jobLauncher,
      @Qualifier("tmdbContentJob") Job tmdbContentJob,
      @Qualifier("sportsContentJob") Job sportsContentJob
  ) {
    this.jobLauncher = jobLauncher;
    this.tmdbContentJob = tmdbContentJob;
    this.sportsContentJob = sportsContentJob;
  }

  @PostMapping("/tmdb")
  public ResponseEntity<String> runTmdbJob() throws Exception {
    JobParameters parameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    jobLauncher.run(tmdbContentJob, parameters);
    return ResponseEntity.ok("TMDB 배치 수동작업 시작");
  }

  @PostMapping("/sports")
  public ResponseEntity<String> runSportsJob() throws Exception {
    JobParameters parameters = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();
    jobLauncher.run(sportsContentJob, parameters);
    return ResponseEntity.ok("Sports 배치 수동작업 시작");
  }
}
