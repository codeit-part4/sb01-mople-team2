package com.sprint.mople.domain.follow.service;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import java.util.UUID;

public interface FollowService {

  FollowResponse follow(UUID followerId, UUID followeeId);

  void unfollow(UUID followerId, UUID followeeId);

}
