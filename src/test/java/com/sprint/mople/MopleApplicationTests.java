package com.sprint.mople;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MopleApplicationTests {

  @Value("${spring.datasource.url}")
  String dbUrl;

  @Test
  void contextLoads() {
    System.out.println("🔍 DB URL: " + dbUrl);
    Assertions.assertTrue(dbUrl.contains("postgres"));
  }

}
