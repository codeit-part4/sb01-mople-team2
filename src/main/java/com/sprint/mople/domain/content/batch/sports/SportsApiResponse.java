package com.sprint.mople.domain.content.batch.sports;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SportsApiResponse {
  @JsonProperty("events")
  private List<SportsItemDto> events;
}
