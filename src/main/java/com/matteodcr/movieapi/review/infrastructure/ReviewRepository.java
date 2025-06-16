package com.matteodcr.movieapi.review.infrastructure;

import com.matteodcr.movieapi.review.api.dto.UpdateReviewDto;
import com.matteodcr.movieapi.review.domain.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
  Review save(Review review, Long tmdbId);

  List<Review> findAll();

  Optional<Review> findById(Long id);

  Optional<Review> update(Long id, UpdateReviewDto dto);

  void delete(Long id);
}
