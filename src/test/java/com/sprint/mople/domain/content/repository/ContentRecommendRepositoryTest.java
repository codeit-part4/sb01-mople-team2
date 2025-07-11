package com.sprint.mople.domain.content.repository;

import com.sprint.mople.global.config.QueryDSLConfig;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Import(QueryDSLConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ContentRecommendRepositoryTest {

  @Container
  static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "none");
    registry.add("spring.sql.init.mode", () -> "always");
    registry.add("spring.sql.init.schema-locations", () -> "classpath:schema.sql");
    registry.add("spring.sql.init.data-locations", () -> "classpath:data.sql");
  }

  @Autowired
  private ContentRecommendRepository recommendRepository;

  @Test
  @DisplayName("findTopRecommended는 높은 점수 순으로 콘텐츠를 반환한다")
  void findTopRecommended() {
    List<Object[]> list = recommendRepository.findTopRecommended(2);
    assertThat(list).hasSize(2);

    // 예상 UUID를 순서대로 명시 (예: 가장 높은 점수 2개)
    UUID[] expectedContentIds = {
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3"), // 예: 4.8점 콘텐츠
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1")  // 예: 4.5점 콘텐츠
    };

    for (int i = 0; i < expectedContentIds.length; i++) {
      UUID contentId = (UUID) list.get(i)[0];
      assertThat(contentId).isEqualTo(expectedContentIds[i]);
    }
  }

  @Test
  @DisplayName("findAllByRecent는 최신 생성 순으로 콘텐츠를 반환한다")
  void findAllByRecent() {
    List<Object[]> list = recommendRepository.findAllByRecent(1);
    assertThat(list).hasSize(1);

    // 가장 최신 생성 콘텐츠 ID 예상값
    UUID expectedContentId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5");

    UUID contentId = (UUID) list.get(0)[0];
    assertThat(contentId).isEqualTo(expectedContentId);
  }

  @Test
  @DisplayName("findAllByReviewCount는 리뷰 많은 콘텐츠를 우선한다")
  void findAllByReviewCount() {
    List<Object[]> list = recommendRepository.findAllByReviewCount(2);
    assertThat(list).hasSize(2);

    // 리뷰 많은 순 상위 2개 콘텐츠 예상 UUID
    UUID[] expectedContentIds = {
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1"), // 리뷰 2개
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2")  // 리뷰 1개
    };

    for (int i = 0; i < expectedContentIds.length; i++) {
      UUID contentId = (UUID) list.get(i)[0];
      assertThat(contentId).isEqualTo(expectedContentIds[i]);
    }
  }

  @Test
  @DisplayName("findAllByScore는 계산된 점수 순으로 콘텐츠를 반환한다")
  void findAllByScore() {
    List<Object[]> list = recommendRepository.findAllByScore(2);
    assertThat(list).hasSize(2);

    // 예상 점수 계산 결과 상위 2개 콘텐츠 UUID (예시)
    UUID[] expectedContentIds = {
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3"),
        UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1")
    };

    for (int i = 0; i < expectedContentIds.length; i++) {
      UUID contentId = (UUID) list.get(i)[0];
      assertThat(contentId).isEqualTo(expectedContentIds[i]);
    }
  }
}
