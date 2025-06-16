package com.matteodcr.movieapi.movie.api.dto.tmdb.image;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TmdbLogoImage {

  @JsonProperty("file_path")
  private String filePath;

  @JsonProperty("iso_639_1")
  private String iso6391;
}
