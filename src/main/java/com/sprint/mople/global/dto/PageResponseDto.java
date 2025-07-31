package com.sprint.mople.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@Builder
public record PageResponseDto<T>(
    @JsonProperty("data") List<T> data,
    @JsonProperty("nextCursor") String nextCursor,
    @JsonProperty("size") int size,
    @JsonProperty("totalElements") long totalElements,
    @JsonProperty("hasNext") boolean hasNext
) {

}
