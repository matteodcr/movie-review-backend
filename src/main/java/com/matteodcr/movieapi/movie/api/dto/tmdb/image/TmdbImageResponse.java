package com.matteodcr.movieapi.movie.api.dto.tmdb.image;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TmdbImageResponse {

  private List<TmdbLogoImage> logos;
  private List<TmdbBackdropImage> backdrops;
}
