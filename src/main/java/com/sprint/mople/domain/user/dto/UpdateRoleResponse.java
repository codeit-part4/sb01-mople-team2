package com.sprint.mople.domain.user.dto;


import com.sprint.mople.domain.user.entity.Role;
import com.sprint.mople.domain.user.entity.UserSource;
import java.time.Instant;
import java.util.UUID;

public record UpdateRoleResponse(
    UUID id,
    Instant createdAt,
    String email,
    String userName,
    Role role,
    UserSource source,
    boolean locked
) {

}
