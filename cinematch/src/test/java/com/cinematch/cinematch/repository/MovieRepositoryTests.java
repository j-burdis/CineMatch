package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
//Tells Spring Boot to replace PSQL db with an in-memory H2 database for this test.
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class MovieRepositoryTests {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsMovieWHenExists() {

        //Arrange
        Movie movie = new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        //Act
        Optional<Movie> foundMovie = movieRepository.findByTitleAndReleaseDate(
                "Test Movie", LocalDate.of(2025, 1, 1)
        );

        //Assert
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getTitle()).isEqualTo("Test Movie");
        assertThat(foundMovie.get().getReleaseDate()).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsEmptyWhenNoMatch() {
        // Arrange
        Movie movie = new Movie(1L, "Some Other Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> foundMovie = movieRepository.findByTitleAndReleaseDate(
                "Non-existent Movie", LocalDate.of(2025, 1, 1)
        );

        // Assert
        assertThat(foundMovie).isEmpty();
    }

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsEmptyWhenTitleDoesNotMatch() {
        // Arrange
        Movie movie = new Movie(1L, "Test Movie Title", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> foundMovie = movieRepository.findByTitleAndReleaseDate(
                "Different Movie Title", LocalDate.of(2025, 1, 1)
        );

        // Assert
        assertThat(foundMovie).isEmpty();
    }

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsEmptyWhenDateDoesNotMatch() {
        // Arrange
        Movie movie = new Movie(1L, "Test Movie Title", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> foundMovie = movieRepository.findByTitleAndReleaseDate(
                "Test Movie Title", LocalDate.of(2024, 1, 1)
        );

        // Assert
        assertThat(foundMovie).isEmpty();
    }

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsMovieWhenReleaseDateIsNull() {
        // Arrange
        Movie movie = new Movie(1L, "Null Date Movie", "Image URL", null, "Dom Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> foundMovie = movieRepository.findByTitleAndReleaseDate(
                "Null Date Movie", null
        );

        // Assert
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getTitle()).isEqualTo("Null Date Movie");
        assertThat(foundMovie.get().getReleaseDate()).isNull();
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsNoMatches() {
        // Arrange
        Movie movie = new Movie(6L, "Completely Different", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        List<Movie> results = movieRepository.searchByTitle("Not Found");

        // Assert
        assertThat(results).isEmpty();
    }


    @Test
    public void MovieRepository_SearchByTitle_ReturnsSingleMovieContainingQuery() {

        // Arrange
        Movie movie1 = new Movie(7L, "Test db Movie", "Test Image URL 1", "2025-01-01", "Test Dom Colour 1");
        movieRepository.save(movie1);

        // Act
        List<Movie> results = movieRepository.searchByTitle("Test db");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Test db Movie");
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsMultipleQueryMatches() {
        // Arrange
        Movie movie1 = new Movie(8L, "Test db Movie One", "url1", "2025-01-01", "Colour1");
        Movie movie2 = new Movie(9L, "Another Test db Film", "url2", "2025-02-01", "Colour2");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Act
        List<Movie> results = movieRepository.searchByTitle("Test db");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Test db Movie One", "Another Test db Film");
    }

    //repository query uses LOWER()
    @Test
    public void MovieRepository_SearchByTitle_ReturnsMovieWhereCaseInsensitive() {
        // Arrange
        Movie movie = new Movie(10L, "UPPER CASE TEST MOVIE", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        List<Movie> results = movieRepository.searchByTitle("case test");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("UPPER CASE TEST MOVIE");
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsMovieForPartialMatch() {
        // Arrange
        Movie movie1 = new Movie(11L, "Tinker, Tailor, Soldier, Spy", "url1", "2025-01-01", "Colour1");
        Movie movie2 = new Movie(12L, "The Spy Who Came in from the Cold", "url2", "2025-02-01", "Colour2");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Act
        List<Movie> result = movieRepository.searchByTitle("Spy");

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Tinker, Tailor, Soldier, Spy", "The Spy Who Came in from the Cold");
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsAllMoviesForEmptyQuery() {
        // Arrange
        Movie movie1 = new Movie(13L, "First Movie", "url1", "2025-01-01", "Colour1");
        Movie movie2 = new Movie(14L, "Second Movie", "url2", "2025-02-01", "Colour2");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Act
        List<Movie> results = movieRepository.searchByTitle("");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("First Movie", "Second Movie");
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsMovieForPartialAndCaseInsensitiveMatch() {
        // Arrange
        Movie movie = new Movie(15L, "A Spy Film", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        List<Movie> result = movieRepository.searchByTitle("sPY fILM");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("A Spy Film");
    }

    @Test
    public void MovieRepository_FindByTitle_ReturnsMovieWhenTitleExists() {

        //Arrange
        Movie movie = new Movie(16L, "Existing Movie", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);
        //Act
        Optional<Movie> result = movieRepository.findByTitle("Existing Movie");
        //Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Existing Movie");
    }

    @Test
    public void MovieRepository_FindByTitle_ReturnsEmptyForNonExistentTitle() {
        //Arrange
        Movie movie = new Movie(17L, "Existing Movie", "url", "2025-01-01", "Colour");
        // Act
        Optional<Movie> result = movieRepository.findByTitle("Different Title");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void MovieRepository_FindByTitle_ReturnsEmptyWhenTitleIsNull() {
        // Arrange
        Movie movie = new Movie(18L, "Some Movie", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> result = movieRepository.findByTitle(null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void MovieRepository_FindByCategory_ReturnsMultipleMoviesMatchingPopularCat() {
        // Arrange
        Movie movie1 = new Movie(19L, "Movie One", "url1", "2025-01-01", "Colour1");
        movie1.setCategory("popular");
        Movie movie2 = new Movie(20L, "Movie Two", "url2", "2025-02-01", "Colour2");
        movie2.setCategory("popular");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        // Act
        List<Movie> results = movieRepository.findByCategory("popular");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Movie One", "Movie Two");
    }

    @Test
    public void MovieRepository_FindByCategory_ReturnsMultipleMoviesMarchingTopRatedCat() {
        Movie movie1 = new Movie(22L, "Top Movie 1", "url1", "2025-03-01", "Colour1");
        movie1.setCategory("top_rated");
        Movie movie2 = new Movie(23L, "Top Movie 2", "url2", "2025-04-01", "Colour2");
        movie2.setCategory("upcoming");
        Movie movie3 = new Movie(24L, "Top Movie 3", "url3", "2025-05-01", "Colour3");
        movie3.setCategory("top_rated");
        movieRepository.save(movie1);
        movieRepository.save(movie2);
        movieRepository.save(movie3);

        List<Movie> results = movieRepository.findByCategory("top_rated");

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("Top Movie 1", "Top Movie 3");
    }

    @Test
    public void MovieRepository_FindByCategory_ReturnsEmptyWhenNoMatch() {
        // Arrange
        Movie movie = new Movie(21L, "Some Movie", "url", "2025-01-01", "Colour");
        movie.setCategory("top_rated");
        movieRepository.save(movie);

        // Act
        List<Movie> result = movieRepository.findByCategory("upcoming");

        // Assert
        assertThat(result).isEmpty();
    }









}

