package com.sprint.mople.domain.follow.service;

import com.sprint.mople.domain.follow.dto.FollowCountResponse;
import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.user.dto.UserListResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface FollowService {

  FollowResponse follow(UUID followerId, UUID followeeId);

  void unfollow(UUID followerId, UUID followeeId);

  Page<UserListResponse> findAllFollowings(UUID userId);

  Page<UserListResponse> findAllFollowers(UUID userId);

  FollowCountResponse getFollowCount(UUID userId);

  Boolean checkFollowing(UUID followerId, UUID followeeId);

}
