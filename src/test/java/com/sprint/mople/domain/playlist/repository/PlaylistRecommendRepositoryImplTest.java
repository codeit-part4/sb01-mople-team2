package com.sprint.mople.domain.playlist.repository;

import com.sprint.mople.domain.playlist.dto.RecommendedPlaylistResponse;
import com.sprint.mople.domain.playlist.entity.Playlist;
import com.sprint.mople.domain.playlist.entity.PlaylistLike;
import com.sprint.mople.domain.playlist.entity.Subscription;
import com.sprint.mople.domain.user.entity.User;
import com.sprint.mople.global.config.QueryDSLConfig;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class PlaylistRecommendRepositoryImplTest {

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
//    registry.add("spring.sql.init.data-locations", () -> "classpath:data.sql");
  }

  @Autowired
  private PlaylistRecommendRepositoryImpl playlistRecommendRepository;

  @Autowired
  private EntityManager em;

  private User user;
  private Playlist playlist1;
  private Playlist playlist2;

  @BeforeEach
  void setUp() {
    em.createNativeQuery("TRUNCATE TABLE playlist_likes CASCADE").executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE subscribes CASCADE").executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE playlists_contents CASCADE").executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE playlists CASCADE").executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE contents CASCADE").executeUpdate();
    em.createNativeQuery("TRUNCATE TABLE users CASCADE").executeUpdate();

    user = User
        .builder()
        .userName("tester")
        .email("test@example.com")
        .build();
    em.persist(user);

    playlist1 = Playlist
        .builder()
        .title("Best Songs")
        .isPublic(true)
        .user(user)
        .build();

    playlist2 = Playlist
        .builder()
        .title("Chill Vibes")
        .isPublic(true)
        .user(user)
        .build();

    em.persist(playlist1);
    em.persist(playlist2);

    // 구독 수 50
    for (int i = 0; i < 50; i++) {
      User subscriber = User
          .builder()
          .userName("subscriber" + i)
          .email("sub" + i + "@mail.com")
          .build();
      em.persist(subscriber);

      Subscription subscription = new Subscription();
      subscription.setUser(subscriber);
      subscription.setPlaylist(playlist1);
      em.persist(subscription);
    }

    // 좋아요 수 30
    for (int i = 0; i < 30; i++) {
      User liker = User
          .builder()
          .userName("liker" + i)
          .email("like" + i + "@mail.com")
          .build();
      em.persist(liker);

      em.persist(new PlaylistLike(liker, playlist1));
    }

    em.flush();
    em.clear();
  }

  @Test
  void 유저의_카테고리에_따른_추천_결과_조회() {
    // given
    List<String> userCategories = List.of("Pop", "Rock");
    int pageSize = 10;

    // when
    List<RecommendedPlaylistResponse> results =
        playlistRecommendRepository.findRecommendedByUserCategoriesWithCursor(
            userCategories,
            null,
            null,
            pageSize
        );

    // then
    assertThat(results).isNotEmpty();
    assertThat(results.size()).isLessThanOrEqualTo(pageSize);

    RecommendedPlaylistResponse first = results.get(0);
    System.out.println("추천된 플레이리스트: " + first.title() + ", 점수: " + first.score());
    assertThat(first.title()).isEqualTo("Best Songs");
    assertThat(first.score()).isGreaterThan(0.0);
  }

  @Test
  void 커서_기반_페이지네이션이_작동하는지_검증() {
    // given
    List<String> userCategories = List.of("Pop", "Rock");
    int pageSize = 1;

    // when
    List<RecommendedPlaylistResponse> firstPage =
        playlistRecommendRepository.findRecommendedByUserCategoriesWithCursor(
            userCategories,
            null,
            null,
            pageSize
        );

    assertThat(firstPage).hasSize(1);

    RecommendedPlaylistResponse first = firstPage.get(0);

    List<RecommendedPlaylistResponse> secondPage =
        playlistRecommendRepository.findRecommendedByUserCategoriesWithCursor(
            userCategories,
            first.score(),
            first.id(),
            pageSize
        );

    // then
    assertThat(secondPage).doesNotContain(first);
  }
}
