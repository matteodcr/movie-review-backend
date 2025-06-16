package com.matteodcr.movieapi.review.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateReviewDto {
  @Size(min = 5, max = 10000)
  private String comment;

  @Min(1)
  @Max(5)
  private int note;
}
