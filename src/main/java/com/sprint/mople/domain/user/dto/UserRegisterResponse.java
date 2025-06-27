package com.sprint.mople.domain.user.dto;

import java.util.UUID;

public record UserRegisterResponse(
    UUID id,
    String name,
    String email
) {

}


