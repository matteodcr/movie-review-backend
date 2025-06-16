package com.matteodcr.movieapi.movie.infrastructure;

import com.matteodcr.movieapi.movie.api.mapper.MovieMapper;
import com.matteodcr.movieapi.movie.domain.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaMovieRepository implements MovieRepository {

  @PersistenceContext private EntityManager em;

  @Override
  public Movie save(Movie review) {
    MovieEntity entity = MovieMapper.toEntity(review);

    MovieEntity existing = em.find(MovieEntity.class, entity.getId());
    if (existing != null) {
      throw new IllegalStateException("Movie with ID " + entity.getId() + " already exists.");
    }

    em.persist(entity);
    return MovieMapper.toDomain(entity);
  }

  @Override
  public Page<Movie> findAll(Pageable pageable) {
    String jpql = "SELECT m FROM MovieEntity m";
    String countJpql = "SELECT COUNT(m) FROM MovieEntity m";

    // Requête principale avec pagination
    List<Movie> content =
        em
            .createQuery(jpql, MovieEntity.class)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList()
            .stream()
            .map(MovieMapper::toDomain)
            .collect(Collectors.toList());

    // Total d’éléments
    Long total = em.createQuery(countJpql, Long.class).getSingleResult();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  @EntityGraph(attributePaths = {"reviews"})
  public Page<Movie> findByTitle(String title, Pageable pageable) {
    String jpql = "SELECT m FROM MovieEntity m WHERE LOWER(m.title) LIKE LOWER(:title)";
    String countJpql = "SELECT COUNT(m) FROM MovieEntity m WHERE LOWER(m.title) LIKE LOWER(:title)";

    List<Movie> content =
        em
            .createQuery(jpql, MovieEntity.class)
            .setParameter("title", "%" + title.toLowerCase() + "%")
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList()
            .stream()
            .map(MovieMapper::toDomain)
            .collect(Collectors.toList());

    Long total =
        em.createQuery(countJpql, Long.class)
            .setParameter("title", "%" + title.toLowerCase() + "%")
            .getSingleResult();

    return new PageImpl<>(content, pageable, total);
  }

  @Override
  public List<Long> findExistingIds(List<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return List.of();
    }

    String jpql = "SELECT m.id FROM MovieEntity m WHERE m.id IN :ids";
    return em.createQuery(jpql, Long.class).setParameter("ids", ids).getResultList();
  }

  @Override
  public Optional<Movie> findById(Long id) {
    return Optional.ofNullable(em.find(MovieEntity.class, id)).map(MovieMapper::toDomain);
  }
}
