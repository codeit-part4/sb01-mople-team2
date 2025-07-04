package com.sprint.mople.domain.review.repository;

import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.review.dto.ContentRatingSummaryDto;
import com.sprint.mople.domain.review.dto.ContentReviewStats;
import com.sprint.mople.domain.review.entity.Review;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

  @Query(
      """
              SELECT new com.sprint.mople.domain.review.dto.ContentRatingSummaryDto(
                  c.id,
                  c.title,
                  COALESCE(AVG(r.rating), 0),
                  COUNT(r)
              )
              FROM Content c
              LEFT JOIN Review r ON r.content.id = c.id
              GROUP BY c.id
          """
  )
  Page<ContentRatingSummaryDto> findContentRatings(Pageable pageable);

  List<Review> findByUser(User user);

  @Query(
      """
              SELECT new com.sprint.mople.domain.review.dto.ContentReviewStats(
                  COALESCE(CAST(AVG(r.rating) AS java.lang.Double), 0.0),
                  COUNT(r)
              )
              FROM Review r
              WHERE r.content.id = :contentId
          """
  )
  ContentReviewStats findReviewStatsByContentId(@Param("contentId") UUID contentId);

  Page<Review> findAllByContent(Content content, Pageable pageable);
}
