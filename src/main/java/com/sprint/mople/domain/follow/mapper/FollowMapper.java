package com.sprint.mople.domain.follow.mapper;

import com.sprint.mople.domain.follow.dto.FollowResponse;
import com.sprint.mople.domain.follow.entity.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {
  public FollowResponse toDto(Follow follow){
    return new FollowResponse(
        follow.getFollower().getId(),
        follow.getFollowee().getId()
    );
  }

}
