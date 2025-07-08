package com.sprint.mople.domain.content.repository;

import com.sprint.mople.domain.content.entity.Content;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRecommendRepository extends JpaRepository<Content, UUID> {

  @Query(value = """
      SELECT
        c.content_id,
        c.title,
        c.created_at,
        c.updated_at,
        c.total_rating_count,
        c.average_rating,
        c.poster_url,
        COALESCE(cl.like_count, 0) AS like_count
      FROM contents c
      LEFT JOIN (
        SELECT content_id, COUNT(*) AS like_count
        FROM content_likes
        GROUP BY content_id
      ) cl ON cl.content_id = c.content_id
      ORDER BY (
        (COALESCE(cl.like_count, 0) / 1000.0) * 0.4 +
        (COALESCE(c.average_rating, 0) / 5.0) * 0.3 +
        (COALESCE(c.total_rating_count, 0) / 500.0) * 0.2 +
        (EXTRACT(EPOCH FROM NOW() - c.updated_at) / 86400)::float * -0.1
      ) DESC
      LIMIT :limit
      """, nativeQuery = true)
  List<Object[]> findTopRecommended(@Param("limit") int limit);

  @Query(value = """
    SELECT
      c.content_id,
      c.title,
      c.created_at,
      c.updated_at,
      COALESCE(cl.like_count, 0) AS like_count,
      (
        (COALESCE(cl.like_count, 0) / 1000.0) * 0.4 +
        (COALESCE(c.average_rating, 0) / 5.0) * 0.3 +
        (COALESCE(c.total_rating_count, 0) / 500.0) * 0.2 +
        (EXTRACT(EPOCH FROM NOW() - c.updated_at) / 86400)::float * -0.1
      ) AS score
    FROM contents c
    LEFT JOIN (
      SELECT content_id, COUNT(*) AS like_count
      FROM content_likes
      GROUP BY content_id
    ) cl ON cl.content_id = c.content_id
    ORDER BY c.created_at DESC
    LIMIT :limit
    """, nativeQuery = true)
  List<Object[]> findAllByRecent(@Param("limit") int limit);

  @Query(value = """
    SELECT
      c.content_id,
      c.title,
      c.created_at,
      c.updated_at,
      COALESCE(cl.like_count, 0) AS like_count,
      (
        (COALESCE(cl.like_count, 0) / 1000.0) * 0.4 +
        (COALESCE(c.average_rating, 0) / 5.0) * 0.3 +
        (COALESCE(c.total_rating_count, 0) / 500.0) * 0.2 +
        (EXTRACT(EPOCH FROM NOW() - c.updated_at) / 86400)::float * -0.1
      ) AS score
    FROM contents c
    LEFT JOIN (
      SELECT content_id, COUNT(*) AS like_count
      FROM content_likes
      GROUP BY content_id
    ) cl ON cl.content_id = c.content_id
    LEFT JOIN (
      SELECT content_id, COUNT(*) AS review_count
      FROM reviews
      GROUP BY content_id
    ) r ON r.content_id = c.content_id
    ORDER BY COALESCE(r.review_count, 0) DESC
    LIMIT :limit
    """, nativeQuery = true)
  List<Object[]> findAllByReviewCount(@Param("limit") int limit);

  @Query(value = """
    SELECT
      c.content_id,
      c.title,
      c.created_at,
      c.updated_at,
      COALESCE(cl.like_count, 0) AS like_count,
      (
        (COALESCE(cl.like_count, 0) / 1000.0) * 0.4 +
        (COALESCE(c.average_rating, 0) / 5.0) * 0.3 +
        (COALESCE(c.total_rating_count, 0) / 500.0) * 0.2 +
        (EXTRACT(EPOCH FROM NOW() - c.updated_at) / 86400)::float * -0.1
      ) AS score
    FROM contents c
    LEFT JOIN (
      SELECT content_id, COUNT(*) AS like_count
      FROM content_likes
      GROUP BY content_id
    ) cl ON cl.content_id = c.content_id
    ORDER BY score DESC
    LIMIT :limit
    """, nativeQuery = true)
  List<Object[]> findAllByScore(@Param("limit") int limit);
}
