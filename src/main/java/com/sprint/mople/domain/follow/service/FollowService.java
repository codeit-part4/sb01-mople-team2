package com.sprint.mople.domain.follow.service;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.user.dto.UserListResponse;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface FollowService {

  FollowResponse follow(UUID followerId, UUID followeeId);

  void unfollow(UUID followerId, UUID followeeId);

  Page<UserListResponse> findAllFollowings(UUID userId, int page, int size);

  Page<UserListResponse> findAllFollowers(UUID userId, int page, int size);

}
