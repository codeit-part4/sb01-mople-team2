package com.sprint.mople.domain.content.batch.sports;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SportsApiRequestInfo {

  private String leagueId;

  private String season;

  private String leagueName;

  private String date;

}
