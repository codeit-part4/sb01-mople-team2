package com.sprint.mople.domain.follow.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record FollowDto(
    @NotBlank(message = "팔로워 아이디가 없습니다.")
    UUID followerId,

    @NotBlank(message = "유저 아이디가 없습니다.")
    UUID followeeId
) {

}
