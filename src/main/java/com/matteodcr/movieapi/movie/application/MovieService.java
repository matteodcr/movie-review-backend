package com.matteodcr.movieapi.movie.application;

import com.matteodcr.movieapi.movie.api.dto.SearchMovieDto;
import com.matteodcr.movieapi.movie.api.dto.tmdb.details.TmdbMovieDetailsResponse;
import com.matteodcr.movieapi.movie.api.dto.tmdb.search.TmdbSearch;
import com.matteodcr.movieapi.movie.api.dto.tmdb.search.TmdbSearchResponse;
import com.matteodcr.movieapi.movie.domain.Movie;
import com.matteodcr.movieapi.movie.infrastructure.MovieRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository repo;

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${tmdb.api.key}")
  private String apiKey;

  public List<Movie> searchTmdbMovie(TmdbSearch dto) {
    String url =
        "https://api.themoviedb.org/3/search/movie?api_key="
            + apiKey
            + "&query="
            + dto.getTitle()
            + "&language=fr-FR";
    ResponseEntity<TmdbSearchResponse> response =
        restTemplate.getForEntity(url, TmdbSearchResponse.class);
    if (response.getBody() == null || response.getBody().getResults() == null) {
      return List.of();
    }
    List<Movie> movies =
        response.getBody().getResults().stream()
            .filter(r -> r.getPopularity() == null || r.getPopularity() >= 2.0)
            .map(
                r ->
                    new Movie(
                        r.getId(),
                        r.getTitle(),
                        null,
                        LocalDate.parse(r.getReleaseDate()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        r.getPosterPath(),
                        null,
                        null,
                        null))
            .collect(Collectors.toList());
    List<Long> tmdbIds = movies.stream().map(Movie::getId).collect(Collectors.toList());

    List<Long> existingIds = repo.findExistingIds(tmdbIds);

    return movies.stream()
        .filter(movie -> !existingIds.contains(movie.getId()))
        .collect(Collectors.toList());
  }

  @Transactional
  public Movie registerMovie(Long tmdbId) {
    String url =
        "https://api.themoviedb.org/3/movie/"
            + tmdbId
            + "?api_key="
            + apiKey
            + "&language=fr-FR"
            + "&append_to_response=credits,release,images"
            + "&include_image_language=fr,en,null";
    ResponseEntity<TmdbMovieDetailsResponse> response =
        restTemplate.getForEntity(url, TmdbMovieDetailsResponse.class);
    assert response.getBody() != null;
    Movie movie =
        new Movie(
            response.getBody().getId(),
            response.getBody().getTitle(),
            response.getBody().getOverview(),
            LocalDate.parse(response.getBody().getReleaseDate()),
            response.getBody().getRuntime(),
            response.getBody().getRevenue(),
            response.getBody().getBudget(),
            response.getBody().getGenres().stream()
                .map(TmdbMovieDetailsResponse.Genre::getName)
                .collect(Collectors.joining(",")),
            response.getBody().getCredits().getCrew().stream()
                .filter(c -> "Director".equals(c.getJob()))
                .map(TmdbMovieDetailsResponse.Crew::getName)
                .collect(Collectors.joining(",")),
            response.getBody().getCredits().getCast().stream()
                .map(TmdbMovieDetailsResponse.Cast::getName)
                .limit(4)
                .collect(Collectors.joining(",")),
            getBestImage(response.getBody().getImages().getPosters(), false),
            getBestImage(response.getBody().getImages().getLogos(), true),
            getBestImage(response.getBody().getImages().getBackdrops(), false),
            null);
    return repo.save(movie);
  }

  public Page<Movie> searchMovie(SearchMovieDto dto, Pageable pageable) {
    return repo.findByTitle(dto.getTitle(), pageable);
  }

  public Page<Movie> getAllMovies(Pageable pageable) {
    return repo.findAll(pageable);
  }

  public Optional<Movie> getMovie(Long id) {
    return repo.findById(id);
  }

  private String getBestImage(
      List<TmdbMovieDetailsResponse.ImageDetail> imageDetails, boolean filterLarger) {
    if (imageDetails == null || imageDetails.isEmpty()) {
      return null;
    }

    // Filtrer les images de type "titre" (plus large que haut)
    List<TmdbMovieDetailsResponse.ImageDetail> filtered =
        imageDetails.stream()
            .filter(
                img ->
                    !filterLarger
                        || (img.getWidth() != null
                            && img.getHeight() != null
                            && img.getWidth() > img.getHeight()))
            .filter(
                img ->
                    img.getVoteAverage() == null
                        || img.getVoteAverage() >= 1.0) // éviter les mauvaises images
            .toList();

    if (filtered.isEmpty()) {
      return null;
    }

    // Prioriser les langues : fr > en > null > autres
    Comparator<TmdbMovieDetailsResponse.ImageDetail> finalComparator = getImageDetailComparator();

    return filtered.stream()
        .sorted(finalComparator)
        .map(TmdbMovieDetailsResponse.ImageDetail::getFilePath)
        .findFirst()
        .orElse(null);
  }

  private static Comparator<TmdbMovieDetailsResponse.ImageDetail> getImageDetailComparator() {
    Comparator<TmdbMovieDetailsResponse.ImageDetail> langComparator =
        Comparator.comparingInt(
            img -> {
              if ("fr".equalsIgnoreCase(img.getIso6391())) {
                return 0;
              }
              if ("en".equalsIgnoreCase(img.getIso6391())) {
                return 1;
              }
              if (img.getIso6391() == null) {
                return 2;
              }
              return 3;
            });

    // Plus de votes (vote_count) et meilleur score (vote_average)
    Comparator<TmdbMovieDetailsResponse.ImageDetail> scoreComparator =
        Comparator.comparingDouble(
                (TmdbMovieDetailsResponse.ImageDetail img) ->
                    img.getVoteAverage() != null ? img.getVoteAverage() : 0.0)
            .thenComparingInt(img -> img.getVoteCount() != null ? img.getVoteCount() : 0)
            .reversed(); // on veut les meilleurs d'abord

    // Combine les comparateurs
    return langComparator.thenComparing(scoreComparator);
  }
}
