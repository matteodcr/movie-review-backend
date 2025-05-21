package com.matteodcr.movieapi.review.api.mapper;

import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import com.matteodcr.movieapi.review.domain.Review;
import com.matteodcr.movieapi.review.infrastructure.ReviewEntity;

public class ReviewMapper {

  public static Review toDomain(ReviewEntity entity) {
    return new Review(
        entity.getId(),
        entity.getTmdbId(),
        entity.getTitle(),
        entity.getComment(),
        entity.getNote());
  }

  public static ReviewEntity toEntity(Review review) {
    return new ReviewEntity(
        review.getId(),
        review.getTmdbId(),
        review.getTitle(),
        review.getComment(),
        review.getNote());
  }

  public static ReviewDto toDto(Review review) {
    ReviewDto dto = new ReviewDto();
    dto.setId(review.getId());
    dto.setTmdbId(review.getTmdbId());
    dto.setTitre(review.getTitle());
    dto.setCommentaire(review.getComment());
    dto.setNote(review.getNote());
    return dto;
  }
}
