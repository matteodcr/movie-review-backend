package com.matteodcr.movieapi.review.api.dto;

import lombok.Data;

@Data
public class ReviewDto {
  private Long id;
  private String comment;
  private int note;
}
