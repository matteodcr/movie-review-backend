package com.matteodcr.movieapi.movie.api.dto.tmdb.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TmdbSearchMovieResult {
  private Long id;
  private String title;

  @JsonProperty("release_date")
  private String releaseDate;

  @JsonProperty("poster_path")
  private String posterPath;

  private Double popularity;
}
