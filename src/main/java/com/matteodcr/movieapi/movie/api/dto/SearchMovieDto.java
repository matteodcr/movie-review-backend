package com.matteodcr.movieapi.movie.api.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchMovieDto {
  @Size(min = 2, max = 50)
  private String title;
}
