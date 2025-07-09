package com.sprint.mople.domain.follow.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mople.domain.follow.entity.QFollow;
import com.sprint.mople.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class FollowRepositoryImpl implements CustomFollowRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<User> findFolloweesByFollower(User follower, Pageable pageable) {
    QFollow follow = QFollow.follow;
    JPAQuery<User> query = queryFactory
        .select(follow.followee)
        .from(follow)
        .where(follow.follower.eq(follower));

    List<User> content = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long count = queryFactory
        .select(follow.count())
        .from(follow)
        .where(follow.follower.eq(follower))
        .fetchOne();

    return new PageImpl<>(content, pageable, count);
  }

  @Override
  public Page<User> findFollowersByFollowee(User followee, Pageable pageable) {
    QFollow follow = QFollow.follow;
    JPAQuery<User> query = queryFactory
        .select(follow.follower)
        .from(follow)
        .where(follow.followee.eq(followee));

    List<User> content = query
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    Long count = queryFactory
        .select(follow.count())
        .from(follow)
        .where(follow.followee.eq(followee))
        .fetchOne();

    return new PageImpl<>(content, pageable, count);
  }
}
