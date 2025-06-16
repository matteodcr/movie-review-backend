package com.matteodcr.movieapi.movie.infrastructure;

import com.matteodcr.movieapi.review.infrastructure.ReviewEntity;
import com.matteodcr.movieapi.shared.Auditable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieEntity extends Auditable {
  @Id private Long id;

  private String title;

  @Column(length = 3000)
  private String overview;

  private LocalDate releaseDate;
  private Integer runtime;

  private Long revenue;
  private Long budget;

  private String genre;
  private String director;
  private String actors;

  private String posterPath;
  private String logoPath;
  private String backdropPath;

  @OneToMany(
      mappedBy = "movie",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  @Builder.Default
  private List<ReviewEntity> reviews = new ArrayList<>();
}
