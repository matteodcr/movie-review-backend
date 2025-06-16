package com.matteodcr.movieapi.review.api;

import static java.util.stream.Collectors.toList;

import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import com.matteodcr.movieapi.review.api.dto.UpdateReviewDto;
import com.matteodcr.movieapi.review.api.mapper.ReviewMapper;
import com.matteodcr.movieapi.review.application.ReviewService;
import com.matteodcr.movieapi.review.domain.Review;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService service;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ReviewDto> create(@RequestBody @Valid CreateReviewDto dto) {
    Review review = service.createReview(dto);
    ReviewDto reviewDto = ReviewMapper.toDto(review);
    return ResponseEntity.created(java.net.URI.create("/reviews/" + review.getId()))
        .body(reviewDto);
  }

  @GetMapping
  public ResponseEntity<List<ReviewDto>> list() {
    List<ReviewDto> reviewDtos =
        service.getAllReviews().stream().map(ReviewMapper::toDto).collect(toList());
    return ResponseEntity.ok(reviewDtos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDto> get(@PathVariable Long id) {
    return service
        .getReview(id)
        .map(r -> ResponseEntity.ok(ReviewMapper.toDto(r)))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.deleteReview(id);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ReviewDto> update(@PathVariable Long id, @RequestBody UpdateReviewDto dto) {
    return service
        .updateReview(id, dto)
        .map(review -> ResponseEntity.ok(ReviewMapper.toDto(review)))
        .orElse(ResponseEntity.notFound().build());
  }
}
