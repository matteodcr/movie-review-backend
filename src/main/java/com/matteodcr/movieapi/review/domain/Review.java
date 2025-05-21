package com.matteodcr.movieapi.review.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Review {
  private Long id;
  private Long tmdbId;
  private String title;
  private String comment;
  private int note;
}
