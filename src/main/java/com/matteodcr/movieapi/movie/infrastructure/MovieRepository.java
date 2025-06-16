package com.matteodcr.movieapi.movie.infrastructure;

import com.matteodcr.movieapi.movie.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepository {
  Movie save(Movie review);

  Page<Movie> findAll(Pageable pageable);

  Page<Movie> findByTitle(String title, Pageable pageable);

  List<Long> findExistingIds(List<Long> ids);

  Optional<Movie> findById(Long id);
}
