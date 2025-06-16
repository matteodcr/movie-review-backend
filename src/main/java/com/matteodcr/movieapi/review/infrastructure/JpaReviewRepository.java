package com.matteodcr.movieapi.review.infrastructure;

import com.matteodcr.movieapi.movie.infrastructure.MovieEntity;
import com.matteodcr.movieapi.review.api.dto.UpdateReviewDto;
import com.matteodcr.movieapi.review.api.mapper.ReviewMapper;
import com.matteodcr.movieapi.review.domain.Review;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaReviewRepository implements ReviewRepository {

  @PersistenceContext private EntityManager em;

  @Override
  public Review save(Review review, Long tmdbId) {
    MovieEntity movieReference = em.getReference(MovieEntity.class, tmdbId);
    ReviewEntity entity = ReviewMapper.toEntity(review, movieReference);
    if (entity.getId() == null) {
      em.persist(entity);
    } else {
      entity = em.merge(entity);
    }
    return ReviewMapper.toDomain(entity);
  }

  @Override
  public List<Review> findAll() {
    return em
        .createQuery("SELECT r FROM ReviewEntity r", ReviewEntity.class)
        .getResultList()
        .stream()
        .map(ReviewMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Review> findById(Long id) {
    return Optional.ofNullable(em.find(ReviewEntity.class, id)).map(ReviewMapper::toDomain);
  }

  @Override
  public Optional<Review> update(Long id, UpdateReviewDto dto) {
    ReviewEntity entity = em.find(ReviewEntity.class, id);
    if (entity != null) {
      entity.setComment(dto.getComment());
      entity.setNote(dto.getNote());
      return Optional.of(ReviewMapper.toDomain(entity));
    }
    return Optional.empty();
  }

  @Override
  public void delete(Long id) {
    ReviewEntity e = em.find(ReviewEntity.class, id);
    if (e != null) {
      MovieEntity movie = e.getMovie();
      if (movie != null) {
        movie.getReviews().remove(e);
      }
      em.remove(e);
    }
  }
}
