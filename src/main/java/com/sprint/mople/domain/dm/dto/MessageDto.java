package com.sprint.mople.domain.dm.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MessageDto(
    @NotBlank(message = "채팅방 아이디가 없습니다.")
    UUID chatRoomId,

    @NotBlank(message = "전송자 아이디가 없습니다.")
    UUID senderId,

    @NotBlank(message = "메세지 내용이 없습니다.")
    String content
) {

}
