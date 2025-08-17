package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class MovieServiceTests {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    public void MovieService_GetAllMoviesFromDatabase_ReturnsMovies() {
        // Arrange: mock repository to return movies
        List<Movie> mockMovies = new ArrayList<>();
        mockMovies.add(new Movie(1L, "Test Movie 1", "Test Poster 1", "2025-01-01", "Dominant Colour" ));
        mockMovies.add(new Movie(2L, "Test Movie 2", "Test Poster 2", "2025-01-01", "Dominant Colour" ));
        when(movieRepository.findAll()).thenReturn(mockMovies);

        // Act
        List<Movie> result = movieService.getAllMoviesFromDatabase();
        // Assert: should return movies from DB - no API call
        assertEquals(mockMovies, result);
        assertEquals(mockMovies, result);
        verify(movieRepository, times(1)).findAll();
    }

@Test
public void MovieService_GetPopularMovies_FetchesFromApiWhenDatabaseIsEmpty() {
    // Spy the real service so we can stub the API call
    MovieService spyService = spy(movieService);

    // Prepare API return data
    List<Movie> apiMovies = List.of(
            new Movie(1L, "Test Movie Title", "Some Poster URL", "2014-11-07", "Dominant Colour")
    );

    // Stub only API fetch method
    doReturn(apiMovies).when(spyService).fetchAndSaveMoviesFromApi("popular");

    // Act: call the method
    List<Movie> result = spyService.getPopularMovies();

    // Assert: result comes from API and API method was called
    assertEquals(apiMovies, result);
    verify(spyService).fetchAndSaveMoviesFromApi("popular");
}

    @Test
    public void MovieService_FindById_ReturnSpecificMovie() {
        Long id = 1L;
        Movie movie = new Movie(id, "Test Movie", "url", "2025-01-01", "Colour");
        Mockito.when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        Movie result = movieService.findById(id);
        assertEquals(movie, result);
    }
}