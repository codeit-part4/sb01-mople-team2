package com.sprint.mople.domain.follow.repository;

import com.sprint.mople.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CustomFollowRepository {

  Page<User> findFolloweesByFollower(@Param("follower") User follower, Pageable pageable);

  Page<User> findFollowersByFollowee(@Param("followee") User followee, Pageable pageable);
}
