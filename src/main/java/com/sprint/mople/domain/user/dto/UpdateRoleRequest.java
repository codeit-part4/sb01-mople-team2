package com.sprint.mople.domain.user.dto;

import com.sprint.mople.domain.user.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {
  private Role role;
}
