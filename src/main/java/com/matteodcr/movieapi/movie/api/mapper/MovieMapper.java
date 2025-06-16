package com.matteodcr.movieapi.movie.api.mapper;

import com.matteodcr.movieapi.movie.api.dto.MovieDto;
import com.matteodcr.movieapi.movie.domain.Movie;
import com.matteodcr.movieapi.movie.infrastructure.MovieEntity;
import com.matteodcr.movieapi.review.api.mapper.ReviewMapper;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;

public class MovieMapper {

  public static Movie toDomain(MovieEntity entity) {
    Movie.MovieBuilder builder =
        Movie.builder()
            .id(entity.getId())
            .title(entity.getTitle())
            .overview(entity.getOverview())
            .releaseDate(entity.getReleaseDate())
            .runtime(entity.getRuntime())
            .revenue(entity.getRevenue())
            .budget(entity.getBudget())
            .genre(entity.getGenre())
            .director(entity.getDirector())
            .actors(entity.getActors())
            .posterPath(entity.getPosterPath())
            .logoPath(entity.getLogoPath())
            .backdropPath(entity.getBackdropPath());

    if (Hibernate.isInitialized(entity.getReviews()) && entity.getReviews() != null) {
      builder.reviews(entity.getReviews().stream().map(ReviewMapper::toDomain).toList());
    }
    return builder.build();
  }

  public static MovieEntity toEntity(Movie movie) {
    MovieEntity.MovieEntityBuilder builder =
        MovieEntity.builder()
            .id(movie.getId())
            .title(movie.getTitle())
            .overview(movie.getOverview())
            .releaseDate(movie.getReleaseDate())
            .runtime(movie.getRuntime())
            .revenue(movie.getRevenue())
            .budget(movie.getBudget())
            .genre(movie.getGenre())
            .director(movie.getDirector())
            .actors(movie.getActors())
            .posterPath(movie.getPosterPath())
            .logoPath(movie.getLogoPath())
            .backdropPath(movie.getBackdropPath());

    var movieEntity = builder.build();

    if (movie.getReviews() != null) {
      builder.reviews(
          movie.getReviews().stream()
              .map(r -> ReviewMapper.toEntity(r, movieEntity))
              .collect(Collectors.toList()));
    }
    return movieEntity;
  }

  public static MovieDto toDto(Movie movie) {
    MovieDto dto = new MovieDto();
    dto.setId(movie.getId());
    dto.setTitle(movie.getTitle());
    dto.setOverview(movie.getOverview());
    dto.setReleaseDate(movie.getReleaseDate());
    dto.setRuntime(movie.getRuntime());
    dto.setRevenue(movie.getRevenue());
    dto.setBudget(movie.getBudget());
    dto.setGenre(movie.getGenre());
    dto.setDirector(movie.getDirector());
    dto.setActors(movie.getActors());
    dto.setPosterPath(movie.getPosterPath());
    dto.setLogoPath(movie.getLogoPath());
    dto.setBackdropPath(movie.getBackdropPath());

    if (movie.getReviews() != null) {
      dto.setReviews(
          movie.getReviews().stream().map(ReviewMapper::toDto).collect(Collectors.toList()));
    }
    return dto;
  }
}
