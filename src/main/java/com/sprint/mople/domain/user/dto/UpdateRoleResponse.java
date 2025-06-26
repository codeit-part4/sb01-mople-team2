package com.sprint.mople.domain.user.dto;

import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.entity.UserSource;
import java.time.Instant;
import java.util.UUID;
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
public class UpdateRoleResponse {
  private UUID id;
  private Instant createdAt;
  private String email;
  private String userName;
  private Role role;
  private UserSource source;
  private boolean locked;
}
