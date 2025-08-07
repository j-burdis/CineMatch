package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.MovieResponse;
import com.cinematch.cinematch.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final String apiKey;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("TMDB_API_KEY");
    }

    public List<Movie> getPopularMovies() {
        // First, try to get movies from database
        List<Movie> existingMovies = movieRepository.findAll();

        // If we have movies in database, return them
        if (!existingMovies.isEmpty()) {
            return existingMovies;
        }

        // Otherwise, fetch from API and save to database
        return fetchAndSaveMoviesFromApi();
    }

    public List<Movie> fetchAndSaveMoviesFromApi() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        MovieResponse response = restTemplate.getForObject(url, MovieResponse.class);

        if (response == null || response.getResults() == null) {
            return List.of();
        }

        return response.getResults().stream()
                .map(MovieResponse.ApiMovie::toEntity)
                .map(this::saveMovieIfNotExists)
                .collect(Collectors.toList());
    }

    private Movie saveMovieIfNotExists(Movie movie) {
        // Check if movie already exists by title and release date
        return movieRepository.findByTitleAndReleaseDate(movie.getTitle(), movie.getReleaseDate())
                .orElseGet(() -> {
                    // Movie doesn't exist, save it
                    return movieRepository.save(movie);
                });
    }

    // Method to force refresh from API (useful for admin purposes)
    public List<Movie> refreshMoviesFromApi() {
        return fetchAndSaveMoviesFromApi();
    }

    // Method to get all movies from database
    public List<Movie> getAllMoviesFromDatabase() {
        return movieRepository.findAll();
    }
}