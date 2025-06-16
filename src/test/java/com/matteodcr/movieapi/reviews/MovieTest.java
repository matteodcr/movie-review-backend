package com.matteodcr.movieapi.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import com.matteodcr.movieapi.movie.api.dto.MovieDto;
import com.matteodcr.movieapi.movie.api.dto.tmdb.search.TmdbSearch;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MovieTest {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/movie";
  }

  @Test
  void shouldSearchMoviesInTmdb() {
    // Arrange
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    TmdbSearch dto = new TmdbSearch();
    dto.setTitle("Inception");

    var request = new HttpEntity<TmdbSearch>(dto, headers);
    ResponseEntity<List<MovieDto>> response =
        restTemplate.exchange(
            baseUrl + "/search/tmdb",
            org.springframework.http.HttpMethod.POST,
            request,
            new ParameterizedTypeReference<List<MovieDto>>() {});

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).extracting(MovieDto::getTitle).contains("Inception");
  }
}
