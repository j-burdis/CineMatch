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
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsSavedMovie() {

        //Arrange
        Movie movie = new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        //Act
        Optional<Movie> found = movieRepository.findByTitleAndReleaseDate(
                "Test Movie", LocalDate.of(2025, 1, 1)
        );

        //Assert
        assertThat(found.isPresent());
        assertThat(found.get().getTitle()).isEqualTo("Test Movie");
        assertThat(found.get().getReleaseDate()).isEqualTo(LocalDate.of(2025, 1, 1));
    }

    @Test
    public void MovieRepository_FindByTitleAndReleaseDate_ReturnsEmptyWhenNotFound() {
        // Arrange
        Movie movie = new Movie(1L, "Some Other Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        // Act
        Optional<Movie> found = movieRepository.findByTitleAndReleaseDate(
                "Non-existent Movie", LocalDate.of(2025, 1, 1)
        );

        // Assert
        assertThat(found).isEmpty();
    }


    @Test
    public void MovieRepository_SearchByTitle_ReturnsSavedMovie() {

        // Arrange
        Movie movie1 = new Movie(1L, "Test db Movie", "Test Image URL 1", "2025-01-01", "Test Dom Colour 1");
        movieRepository.save(movie1);

        // Act
        List<Movie> results = movieRepository.searchByTitle("Test db");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("Test db Movie");
    }

    @Test
    public void MovieRepository_SearchByTitle_ReturnsMultipleMatches() {
        // Arrange
        Movie movie1 = new Movie(1L, "Test db Movie One", "url1", "2025-01-01", "Colour1");
        Movie movie2 = new Movie(2L, "Another Test db Film", "url2", "2025-02-01", "Colour2");
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

    @Test
    public void MovieRepository_SearchByTitle_ReturnsNoMatches() {
        // Arrange
        Movie movie = new Movie(1L, "Completely Different", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        List<Movie> results = movieRepository.searchByTitle("Not Found");

        // Assert
        assertThat(results).isEmpty();
    }

    //repository query uses LOWER()
    @Test
    public void MovieRepository_SearchByTitle_ReturnsMovieCaseInsensitive() {
        // Arrange
        Movie movie = new Movie(1L, "UPPER CASE TEST MOVIE", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);

        // Act
        List<Movie> results = movieRepository.searchByTitle("case test");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitle()).isEqualTo("UPPER CASE TEST MOVIE");
    }

//    Optional<Movie> findByTitle(String title);
    @Test
    public void MovieRepository_FindByTitle_ReturnsMovie() {

        //Arrange
        Movie movie = new Movie(1L, "Another Movie", "url", "2025-01-01", "Colour");
        movieRepository.save(movie);
        //Act
        Optional<Movie> found = movieRepository.findByTitle("Another Movie");
        //Assert
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Another Movie");
    }


    @Test
    public void MovieRepository_FindByTitle_ReturnsEmptyForNonExistentTitle() {
        // Act
        Optional<Movie> found = movieRepository.findByTitle("Non-Existent Title");

        // Assert
        assertThat(found).isEmpty();
    }
}

