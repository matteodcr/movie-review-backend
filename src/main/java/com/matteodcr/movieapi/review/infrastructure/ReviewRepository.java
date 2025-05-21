package com.matteodcr.movieapi.review.infrastructure;

import com.matteodcr.movieapi.review.domain.Review;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
  Review save(Review review);

  List<Review> findAll();

  Optional<Review> findById(Long id);

  void delete(Long id);
}
