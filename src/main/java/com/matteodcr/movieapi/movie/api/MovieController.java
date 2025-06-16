package com.matteodcr.movieapi.movie.api;

import static java.util.stream.Collectors.toList;

import com.matteodcr.movieapi.movie.api.dto.MovieDto;
import com.matteodcr.movieapi.movie.api.dto.SearchMovieDto;
import com.matteodcr.movieapi.movie.api.dto.tmdb.search.TmdbSearch;
import com.matteodcr.movieapi.movie.api.mapper.MovieMapper;
import com.matteodcr.movieapi.movie.application.MovieService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService service;

  @PostMapping("/search/tmdb")
  public ResponseEntity<List<MovieDto>> searchTmdb(@RequestBody TmdbSearch dto) {
    return ResponseEntity.ok(
        service.searchTmdbMovie(dto).stream().map(MovieMapper::toDto).collect(toList()));
  }

  @PostMapping("/search")
  public ResponseEntity<Page<MovieDto>> search(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestBody @Valid SearchMovieDto dto) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return ResponseEntity.ok(service.searchMovie(dto, pageable).map(MovieMapper::toDto));
  }

  @GetMapping
  public ResponseEntity<Page<MovieDto>> list(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    return ResponseEntity.ok(service.getAllMovies(pageable).map(MovieMapper::toDto));
  }

  @GetMapping("/{id}")
  public ResponseEntity<MovieDto> get(@PathVariable Long id) {
    return service
        .getMovie(id)
        .map(r -> ResponseEntity.ok(MovieMapper.toDto(r)))
        .orElse(ResponseEntity.notFound().build());
  }
}
