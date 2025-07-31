package com.sprint.mople.domain.content.batch.sports;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SportsItemDto {

  @JsonProperty("strFilename")
  private String filename;

  @JsonProperty("strVenue")
  private String venue;

  @JsonProperty("strLeague")
  private String leagueName;

  @JsonProperty("strHomeTeam")
  private String homeTeam;

  @JsonProperty("strAwayTeam")
  private String awayTeam;

  @JsonProperty("intHomeScore")
  private String homeScore;

  @JsonProperty("intAwayScore")
  private String awayScore;

  @JsonProperty("dateEvent")
  private String eventDate;

  @JsonProperty("strTime")
  private String utcTime;

  @JsonProperty("strTimeLocal")
  private String localTime;

  @JsonProperty("strVideo")
  private String videoUrl;

  @JsonProperty("strThumb")
  private String thumbnailUrl;

}
