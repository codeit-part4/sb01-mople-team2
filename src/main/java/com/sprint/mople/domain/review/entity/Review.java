package com.sprint.mople.domain.review.entity;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor
public class Review {

  @Id
  @Column(name = "review_id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "content_id", nullable = false)
  private Content content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "comment", length = 1000)
  private String comment;

  @Column(name = "created_at")
  private Instant createdAt;

  @Builder
  public Review(
      Content content,
      User user,
      Integer rating,
      String comment
  )
  {
    this.content = content;
    this.user = user;
    this.rating = rating;
    this.comment = comment;
  }

  public void updateComment(String comment) {
    this.comment = comment;
  }

  public void updateRating(int rating) {
    this.rating = rating;
  }
}
