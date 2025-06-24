package com.sprint.mople.domain.content.entity;

import com.sprint.mople.global.util.StringSetConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contents")
@Getter
@NoArgsConstructor
public class Content {

  @Id
  @Column(name = "content_id", columnDefinition = "uuid")
  @GeneratedValue
  private UUID id;

  @Column(name = "external_id")
  private String externalId;

  @Enumerated(EnumType.STRING)
  @Column(length = 25)
  private Source source;

  @Column
  private String title;

  @Column(columnDefinition = "TEXT")
  private String summary;

  @Enumerated(EnumType.STRING)
  @Column(length = 25)
  private Category category;

  @Column(name = "poster_url")
  private String posterUrl;

  @Column(name = "genres", columnDefinition = "jsonb")
  @Convert(converter = StringSetConverter.class)
  private Set<String> genres;

  @Column(name = "released_at")
  private Instant releasedAt;

  @Column(name = "created_at", columnDefinition = "timestamp with time zone")
  private Instant createdAt;

  @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
  private Instant updatedAt;

  @Column
  private Integer rating;

  public enum Source {
    TMDB,
    THE_SPORTS_DB
  }

  public enum Category {
    MOVIE,
    TV,
    SPORTS
  }
}
