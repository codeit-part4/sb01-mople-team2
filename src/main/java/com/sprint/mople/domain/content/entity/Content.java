package com.sprint.mople.domain.content.entity;

import com.sprint.mople.domain.playlist.entity.PlaylistContent;
import com.sprint.mople.global.util.StringSetConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "contents", uniqueConstraints = @UniqueConstraint(
    columnNames = {
        "external_id",
        "source"
    }
)
)
@Getter
@NoArgsConstructor
public class Content {

  @Id
  @Column(name = "content_id", columnDefinition = "uuid")
  @GeneratedValue(strategy = GenerationType.UUID)
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

  @Column(name = "released_at", columnDefinition = "timestamp with time zone")
  private Instant releasedAt;

  @Column(name = "created_at", columnDefinition = "timestamp with time zone")
  private Instant createdAt;

  @Column(name = "updated_at", columnDefinition = "timestamp with time zone")
  private Instant updatedAt;

  @Column(precision = 3, scale = 2)
  private BigDecimal averageRating;

  @Column
  private Long totalRatingCount;

  @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<PlaylistContent> playlistContents = new ArrayList<>();

  @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<ContentLike> contentLikes = new ArrayList<>();

  @Builder
  public Content(
      String externalId,
      Source source,
      String title,
      String summary,
      Category category
      )
  {
    this.externalId = externalId;
    this.source = source;
    this.title = title;
    this.summary = summary;
    this.category = category;
    this.averageRating = BigDecimal.ZERO;
    this.totalRatingCount = 0L;
  }

  public enum Source {
    TMDB,
    THE_SPORTS_DB
  }

  public enum Category {
    MOVIE,
    TV,
    SPORTS,
    SHOW
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void updateAverageRating(BigDecimal averageRating) {
    this.averageRating = averageRating;
  }

  public void updateTotalRatingCount(Long totalRatingCount) {
    this.totalRatingCount = totalRatingCount;
  }
}
