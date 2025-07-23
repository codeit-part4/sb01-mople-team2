package com.sprint.mople.domain.user.dto;

import com.sprint.mople.domain.user.entity.User;
import java.time.Instant;

public record UserListResponse(
    String userName,
    String email,
    Boolean isLocked,
    Instant createAt
)
{

  public static UserListResponse from(User user) {
    return new UserListResponse(
        user.getUserName(),
        user.getEmail(),
        user.getIsLocked(),
        user.getCreateAt()
    );
  }
}

