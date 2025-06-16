package com.matteodcr.movieapi.movie.api.dto.tmdb.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TmdbMovieDetailsResponse {
  private Long id;
  private String title;

  @JsonProperty("release_date")
  private String releaseDate;

  @JsonProperty("poster_path")
  private String posterPath;

  @JsonProperty("backdrop_path")
  private String backdropPath;

  private String overview;
  private Integer runtime;
  private Long revenue;
  private Long budget;

  @JsonProperty("vote_average")
  private Double voteAverage;

  @JsonProperty("vote_count")
  private Integer voteCount;

  private List<Genre> genres;

  @JsonProperty("production_countries")
  private List<ProductionCountry> productionCountries;

  @JsonProperty("production_companies")
  private List<ProductionCompany> productionCompanies;

  private Credits credits;
  private Releases releases;
  private Images images;

  @Data
  public static class Genre {
    private Integer id;
    private String name;
  }

  @Data
  public static class ProductionCountry {
    @JsonProperty("iso_3166_1")
    private String iso31661;

    private String name;
  }

  @Data
  public static class ProductionCompany {
    private Integer id;
    private String name;

    @JsonProperty("logo_path")
    private String logoPath;

    @JsonProperty("origin_country")
    private String originCountry;
  }

  @Data
  public static class Credits {
    private List<Cast> cast;
    private List<Crew> crew;
  }

  @Data
  public static class Cast {
    private Integer id;
    private String name;
    private String character;

    @JsonProperty("profile_path")
    private String profilePath;

    private Integer order;

    @JsonProperty("cast_id")
    private Integer castId;

    @JsonProperty("credit_id")
    private String creditId;
  }

  @Data
  public static class Crew {
    private Integer id;
    private String name;
    private String job;
    private String department;

    @JsonProperty("profile_path")
    private String profilePath;

    @JsonProperty("credit_id")
    private String creditId;
  }

  @Data
  public static class Releases {
    private List<Release> countries;
  }

  @Data
  public static class Release {
    @JsonProperty("iso_3166_1")
    private String iso31661;

    @JsonProperty("release_dates")
    private List<ReleaseDate> releaseDates;
  }

  @Data
  public static class ReleaseDate {
    private String certification;

    @JsonProperty("release_date")
    private String releaseDate;

    private Integer type;
    private String note;
  }

  @Data
  public static class Images {
    private List<ImageDetail> backdrops;
    private List<ImageDetail> posters;
    private List<ImageDetail> logos;
  }

  @Data
  public static class ImageDetail {
    @JsonProperty("aspect_ratio")
    private Double aspectRatio;

    @JsonProperty("file_path")
    private String filePath;

    private Integer height;
    private Integer width;

    @JsonProperty("iso_639_1")
    private String iso6391;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;
  }
}
