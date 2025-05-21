package com.matteodcr.movieapi.review.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CreateReviewDto {
  private Long tmdbId;
  private String titre;
  private String commentaire;

  @Min(1)
  @Max(5)
  private int note;
}
