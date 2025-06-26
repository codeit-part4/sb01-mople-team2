package com.sprint.mople.domain.user.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserListResponseDto {
  private String userName;
  private String email;
  private Boolean isLocked;
  private Instant createAt;
}
