package com.matteodcr.movieapi.movie.api.dto.tmdb.search;

import java.util.List;
import lombok.Data;

@Data
public class TmdbSearchResponse {
  private List<TmdbSearchMovieResult> results;
}
