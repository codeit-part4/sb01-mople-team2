package com.sprint.mople.domain.dm.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record MessageCreateRequest(
    @NotBlank(message = "보내는이 아이디가 없습니다.") UUID senderId,
    @NotBlank(message = "받는이 아이디가 없습니다.") UUID receiverId,
    @NotBlank(message = "내용이 없습니다.") String content
) {

}
