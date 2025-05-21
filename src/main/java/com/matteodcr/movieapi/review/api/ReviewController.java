package com.matteodcr.movieapi.review.api;

import static java.util.stream.Collectors.toList;

import com.matteodcr.movieapi.review.api.dto.CreateReviewDto;
import com.matteodcr.movieapi.review.api.dto.ReviewDto;
import com.matteodcr.movieapi.review.api.mapper.ReviewMapper;
import com.matteodcr.movieapi.review.application.ReviewService;
import com.matteodcr.movieapi.review.domain.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService service;

  @PostMapping
  public ResponseEntity<ReviewDto> create(@RequestBody CreateReviewDto dto) {
    Review review = service.createReview(dto);
    return ResponseEntity.ok(ReviewMapper.toDto(review));
  }

  @GetMapping
  public List<ReviewDto> list() {
    return service.getAllReviews().stream().map(ReviewMapper::toDto).collect(toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDto> get(@PathVariable Long id) {
    return service
        .getReview(id)
        .map(r -> ResponseEntity.ok(ReviewMapper.toDto(r)))
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.deleteReview(id);
    return ResponseEntity.noContent().build();
  }
}
