package com.matteodcr.movieapi.review.api.dto;

import lombok.Data;

@Data
public class ReviewDto {
  private Long id;
  private Long tmdbId;
  private String titre;
  private String commentaire;
  private int note;
}
