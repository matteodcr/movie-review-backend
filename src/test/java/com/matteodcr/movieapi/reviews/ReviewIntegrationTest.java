package com.matteodcr.movieapi.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReviewIntegrationTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  void shouldCreateAndFetchReview() {
    // Arrange
    CreateReviewDto dto = new CreateReviewDto();
    dto.setTitre("Inception");
    dto.setCommentaire("Super film");
    dto.setNote(5);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<CreateReviewDto> request = new HttpEntity<>(dto, headers);

    // Act
    ResponseEntity<ReviewDto> response =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/reviews", request, ReviewDto.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getTitre()).isEqualTo("Inception");
  }

  @Test
  void shouldCreateAndDeleteReview() {
    // Arrange: create a review
    CreateReviewDto dto = new CreateReviewDto();
    dto.setTitre("Matrix");
    dto.setCommentaire("Excellent");
    dto.setNote(4);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CreateReviewDto> request = new HttpEntity<>(dto, headers);

    // Act: create review
    ResponseEntity<ReviewDto> createResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/reviews", request, ReviewDto.class);
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    Assertions.assertNotNull(createResponse.getBody());
    Long reviewId = createResponse.getBody().getId();

    // Act: delete review
    restTemplate.delete("http://localhost:" + port + "/reviews/" + reviewId);

    // Assert: try to fetch deleted review
    ResponseEntity<ReviewDto> fetchResponse =
        restTemplate.getForEntity(
            "http://localhost:" + port + "/reviews/" + reviewId, ReviewDto.class);
    assertThat(fetchResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
