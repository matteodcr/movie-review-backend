package com.matteodcr.movieapi.movie.api.dto;

import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class MovieDto {
  private Long id;
  private String title;
  private String overview;

  private LocalDate releaseDate;
  private Integer runtime;

  private Long revenue;
  private Long budget;

  private String genre;
  private String director;
  private String actors;

  private String posterPath;
  private String logoPath;
  private String backdropPath;

  private List<ReviewDto> reviews;
}
