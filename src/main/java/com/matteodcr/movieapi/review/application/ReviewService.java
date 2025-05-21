package com.matteodcr.movieapi.review.application;

import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.domain.Review;
import com.matteodcr.movieapi.review.infrastructure.ReviewRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository repo;

  @Transactional
  public Review createReview(CreateReviewDto dto) {
    Review review =
        new Review(null, dto.getTmdbId(), dto.getTitre(), dto.getCommentaire(), dto.getNote());
    return repo.save(review);
  }

  public List<Review> getAllReviews() {
    return repo.findAll();
  }

  public Optional<Review> getReview(Long id) {
    return repo.findById(id);
  }

  @Transactional
  public void deleteReview(Long id) {
    repo.delete(id);
  }
}
