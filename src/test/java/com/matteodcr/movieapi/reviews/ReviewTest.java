package com.matteodcr.movieapi.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import com.matteodcr.movieapi.review.api.dto.UpdateReviewDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
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
public class ReviewTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/reviews";
  }

  @Test
  void shouldCreateAndFetchReview() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Arrange
    CreateReviewDto dto = new CreateReviewDto();
    dto.setComment("Super film");
    dto.setNote(5);
    dto.setTmdbId((long) 870028);

    HttpEntity<CreateReviewDto> request = new HttpEntity<>(dto, headers);

    // Act
    ResponseEntity<ReviewDto> response =
        restTemplate.postForEntity(baseUrl, request, ReviewDto.class);

    // Assert
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldCreateAndDeleteReview() {
    // Arrange: create a review
    CreateReviewDto dto = new CreateReviewDto();
    dto.setComment("Excellent");
    dto.setNote(4);
    dto.setTmdbId((long) 870028);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<CreateReviewDto> request = new HttpEntity<>(dto, headers);

    // Act: create review
    ResponseEntity<ReviewDto> createResponse =
        restTemplate.postForEntity(baseUrl, request, ReviewDto.class);
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(createResponse.getBody()).isNotNull();
    Long reviewId = createResponse.getBody().getId();

    // Act: delete review
    restTemplate.exchange(
        baseUrl + "/" + reviewId, org.springframework.http.HttpMethod.DELETE, null, Void.class);

    // Assert: try to fetch deleted review
    ResponseEntity<ReviewDto> fetchResponse =
        restTemplate.getForEntity(baseUrl + "/" + reviewId, ReviewDto.class);
    assertThat(fetchResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void shouldReturnNotFoundForNonExistentReview() {
    // Act: try to fetch a non-existent review
    ResponseEntity<ReviewDto> response =
        restTemplate.getForEntity(baseUrl + "/999999", ReviewDto.class);

    // Assert: expect NOT_FOUND status
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNull();
  }

  @Test
  void shouldCreateAndPatchReview() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Arrange: create a review
    CreateReviewDto dto = new CreateReviewDto();
    dto.setComment("Great movie");
    dto.setNote(4);
    dto.setTmdbId((long) 870028);

    HttpEntity<CreateReviewDto> request = new HttpEntity<>(dto, headers);
    ResponseEntity<ReviewDto> createResponse =
        restTemplate.postForEntity(baseUrl, request, ReviewDto.class);
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    Assertions.assertNotNull(createResponse.getBody());
    Long reviewId = createResponse.getBody().getId();

    // Act: patch the review
    UpdateReviewDto patchDto = new UpdateReviewDto();
    patchDto.setComment("Updated comment");
    patchDto.setNote(5);

    HttpEntity<UpdateReviewDto> patchRequest = new HttpEntity<>(patchDto, headers);
    ResponseEntity<ReviewDto> patchResponse =
        restTemplate.exchange(
            baseUrl + "/" + reviewId,
            org.springframework.http.HttpMethod.PATCH,
            patchRequest,
            ReviewDto.class);

    // Assert: check the updated review
    assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(patchResponse.getBody()).isNotNull();
    assertThat(patchResponse.getBody().getComment()).isEqualTo("Updated comment");
  }
}
