package com.sprint.mople.domain.content.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mople.domain.content.entity.Content;
import com.sprint.mople.domain.content.entity.QContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContentRepositoryImpl implements ContentRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Content> findContentsWithCursor(String title, String cursorValue, UUID cursorId, int size) {
    QContent content = QContent.content;

    BooleanExpression filter = null;
    if (hasText(title)) {
      BooleanExpression titleMatch = content.normalizedTitle.containsIgnoreCase(title);
      BooleanExpression summaryMatch = content.summary.containsIgnoreCase(title);
      filter = titleMatch.or(summaryMatch);
    }

    BooleanExpression cursorCondition = buildCursorCondition(content, cursorValue, cursorId);

    return queryFactory
        .selectFrom(content)
        .where(filter, cursorCondition)
        .orderBy(content.normalizedTitle.asc(), content.id.asc())
        .limit(size)
        .fetch();
  }

  @Override
  public long countContentsByTitle(String title) {
    QContent content = QContent.content;

    BooleanExpression filter = null;
    if (hasText(title)) {
      BooleanExpression titleMatch = content.normalizedTitle.containsIgnoreCase(title);
      BooleanExpression summaryMatch = content.summary.containsIgnoreCase(title);
      filter = titleMatch.or(summaryMatch);
    }

    return Optional.ofNullable(queryFactory
        .select(content.count())
        .from(content)
        .where(filter)
        .fetchOne()).orElse(0L);
  }

  private BooleanExpression buildCursorCondition(QContent content, String cursorValue, UUID cursorId) {
    if (!hasText(cursorValue) || cursorId == null) return null;
    return content.normalizedTitle.gt(cursorValue)
        .or(content.normalizedTitle.eq(cursorValue).and(content.id.gt(cursorId)));
  }

  private boolean hasText(String str) {
    return str != null && !str.trim().isEmpty();
  }
}