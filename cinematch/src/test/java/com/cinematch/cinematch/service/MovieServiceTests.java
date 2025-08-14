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
        // ensures method is not called
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


//    public Movie findById(Long movieId) {
//        return movieRepository.findById(movieId).orElse(null);
//    }
    @Test
    void shouldReturnMovieWhenFound() {
        Long id = 1L;
        Movie movie = new Movie(id, "Test Movie", "url", "2025-01-01", "Colour");
        //Informs mock movieRepository that if findById(1L) is called, return Optional that contains that movie
        Mockito.when(movieRepository.findById(id)).thenReturn(Optional.of(movie));
        //calls the real movieService.findById() method which calls the mocked repository - because of @Mock private MovieRepository movieRepository;
        Movie result = movieService.findById(id);
        //checks movie returned by service is same object in Movie movie = new Movie()
        assertEquals(movie, result);
    }
}