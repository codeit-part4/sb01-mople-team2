package com.sprint.mople.domain.content.batch.tmdb;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TmdbGenreResponse {
  private List<TmdbGenreDto> genres;
}