package com.matteodcr.movieapi.review.application;

import com.matteodcr.movieapi.movie.application.MovieService;
import com.matteodcr.movieapi.movie.domain.Movie;
import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.api.dto.UpdateReviewDto;
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
  private final MovieService movieService;

  @Transactional
  public Review createReview(CreateReviewDto dto) {
    Movie movie = movieService.registerMovie(dto.getTmdbId());

    Review review = new Review(null, dto.getComment(), dto.getNote());

    return repo.save(review, movie.getId());
  }

  public List<Review> getAllReviews() {
    return repo.findAll();
  }

  public Optional<Review> getReview(Long id) {
    return repo.findById(id);
  }

  @Transactional
  public Optional<Review> updateReview(Long id, UpdateReviewDto dto) {
    return repo.update(id, dto);
  }

  @Transactional
  public void deleteReview(Long id) {
    repo.delete(id);
  }
}
