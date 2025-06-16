package com.matteodcr.movieapi.movie.domain;

import com.matteodcr.movieapi.review.domain.Review;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Movie {
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

  private List<Review> reviews;
}
