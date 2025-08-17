package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@DataJpaTest
//Tells Spring Boot to replace PSQL db with an in-memory H2 database for this test.
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

public class ColourRepositoryTests {

    @Autowired
    private ColourRepository colourRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void ColourRepository_FindByMovie_ReturnsSingleColourWhenExists() {
        Movie movie = new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        ColourModel colour = new ColourModel();
        colour.setMovie(movie);
        colour.setColour_1("#FF5733");
        colourRepository.save(colour);

        Optional<ColourModel> result = colourRepository.findByMovie(movie);

        assertThat(result).isPresent();
        assertThat(result.get().getColour_1()).isEqualTo("#FF5733");
    }

    @Test
    public void ColourRepository_FindByMovie_ReturnsMultipleColoursWhenExist() {
        Movie movie = new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Test Dom Colour");
        movieRepository.save(movie);

        ColourModel colour = new ColourModel();
        colour.setMovie(movie);
        colour.setColour_1("#FF5733");
        colour.setColour_2("#FF5734");
        colour.setColour_3("#FF5735");
        colour.setColour_4("#FF5736");
        colour.setColour_5("#FF5737");
        colourRepository.save(colour);

        Optional<ColourModel> result = colourRepository.findByMovie(movie);

        assertThat(result).isPresent();
        assertThat(result.get().getColour_1()).isEqualTo("#FF5733");
        assertThat(result.get().getColour_2()).isEqualTo("#FF5734");
        assertThat(result.get().getColour_3()).isEqualTo("#FF5735");
        assertThat(result.get().getColour_4()).isEqualTo("#FF5736");
        assertThat(result.get().getColour_5()).isEqualTo("#FF5737");
    }

    @Test
    public void ColourRepository_FindByMovie_ReturnsEmptyWhenNoneExists() {

        Movie movie = new Movie(2L, "Another Movie", "Some URL", "2025-02-02", "Blue");
        movieRepository.save(movie);

        Optional<ColourModel> result = colourRepository.findByMovie(movie);

        assertThat(result).isEmpty();
    }

    @Test
    public void ColourRepository_FindByMovie_ReturnsCorrectColourForMultipleMovies() {
        Movie movie1 = new Movie(3L, "Movie One", "URL1", "2025-01-01", "Red");
        Movie movie2 = new Movie(4L, "Movie Two", "URL2", "2025-02-02", "Blue");
        movieRepository.save(movie1);
        movieRepository.save(movie2);

        ColourModel colour1 = new ColourModel();
        colour1.setMovie(movie1);
        colour1.setColour_1("#FF0000");
        colourRepository.save(colour1);

        ColourModel colour2 = new ColourModel();
        colour2.setMovie(movie2);
        colour2.setColour_1("#0000FF");
        colourRepository.save(colour2);

        Optional<ColourModel> result1 = colourRepository.findByMovie(movie1);
        Optional<ColourModel> result2 = colourRepository.findByMovie(movie2);

        assertThat(result1).isPresent();
        assertThat(result1.get().getColour_1()).isEqualTo("#FF0000");
        assertThat(result2).isPresent();
        assertThat(result2.get().getColour_1()).isEqualTo("#0000FF");
    }

    @Test
    public void ColourRepository_ExistsByMovie_ReturnsTrueWhenColoursExist() {
        Movie movie = new Movie(5L, "Test Movie", "URL", "2025-01-01", "DomColour");
        movieRepository.save(movie);

        ColourModel colour = new ColourModel();
        colour.setMovie(movie);
        colour.setColour_1("#FF5733");
        colourRepository.save(colour);

        boolean exists = colourRepository.existsByMovie(movie);

        assertThat(exists).isTrue();
    }

    @Test
    public void ColourRepository_ExistsByMovie_ReturnsFalseWhenNoColoursExist() {
        Movie movie = new Movie(6L, "Another Movie", "URL", "2025-02-02", "DomColour");
        movieRepository.save(movie);

        boolean exists = colourRepository.existsByMovie(movie);

        assertThat(exists).isFalse();
    }
}


