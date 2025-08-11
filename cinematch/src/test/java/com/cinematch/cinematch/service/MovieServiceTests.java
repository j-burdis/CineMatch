package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class MovieServiceTests {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    public void shouldReturnPopularMoviesWhenExistInDatabase() {
        // Arrange: mock repository to return movies
        List<Movie> mockMovies = new ArrayList<>();
        mockMovies.add(new Movie(1L, "Test Movie Title", "Test Poster", "2025-01-01", "Dominant Colour" ));
        when(movieRepository.findAll()).thenReturn(mockMovies);

        //Spy service
        MovieService spyService = spy(movieService);

        // Act
        List<Movie> result = movieService.getPopularMovies();
        // Assert: should return movies from DB - no API call
        assertEquals(mockMovies, result);
        // ensure method not called
        verify(spyService, never()).fetchAndSaveMoviesFromApi();
    }

    @Test
    void shouldFetchMoviesFromApiWhenDatabaseIsEmpty() {
        // Arrange: empty db
        when(movieRepository.findAll()).thenReturn(new ArrayList<>());
        // mock fetchAndSaveMoviesFromApi()
        MovieService spyService = spy(movieService);
        List<Movie> apiMovies = List.of(
                new Movie(1L, "Test Movie Title", "Some Poster URL", "2014-11-07", "Dominant Colour")
        );
        doReturn(apiMovies).when(spyService).fetchAndSaveMoviesFromApi();
        // Act
        List<Movie> result = spyService.getPopularMovies();
        // Assert
        assertEquals(apiMovies, result);
        verify(spyService).fetchAndSaveMoviesFromApi();
    }
}