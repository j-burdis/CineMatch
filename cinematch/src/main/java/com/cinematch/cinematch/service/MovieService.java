package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.ApiMovie;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.MovieResponse;
import com.cinematch.cinematch.repository.MovieRepository;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MovieService {

    private final String apiKey;
    private final MovieRepository movieRepository;
    private final ImageService imageService;

    @Autowired
    public MovieService(MovieRepository movieRepository,ImageService imageService) {
        this.movieRepository = movieRepository;
        this.imageService = imageService;
//        Dotenv dotenv = Dotenv.load();
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
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
                .map(ApiMovie::toEntity)
                .map(this::saveMovieIfNotExists)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Movie saveMovieIfNotExists(Movie movie) {
        if (movie.getPosterUrl() == null || movie.getPosterUrl().isBlank()) {
            return null; // skip save if poster url is blank
        }

        // check if movie already exists by title and release date
        return movieRepository.findById(movie.getId())
                .orElseGet(() -> {
                    // movie doesn't exist, save it
                    String dominantColour = imageService.getDominantColour(movie.getPosterUrl());
                    movie.setDominantColour(dominantColour);
                    return movieRepository.save(movie);
                });
    }

    // force refresh from API (for admin purposes)
    public List<Movie> refreshMoviesFromApi() {
        return fetchAndSaveMoviesFromApi();
    }

    // get all movies from database
    public List<Movie> getAllMoviesFromDatabase() {
        return movieRepository.findAll();
    }

    public Movie findById(Long movieId) {
        return movieRepository.findById(movieId).orElse(null);
    }

    public List<Movie> searchMovies(String query) {
        // search DB first
        List<Movie> dbResults = movieRepository.searchByTitle(query);

        // query TMDb too (could be rate-limited if needed)
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + query;
        RestTemplate restTemplate = new RestTemplate();
        MovieResponse response = restTemplate.getForObject(url, MovieResponse.class);

        List<Movie> apiResults = List.of();
        if (response != null && response.getResults() != null) {
            apiResults = response.getResults().stream()
                    // skip movies with no poster
                    .filter(apiMovie -> apiMovie.getPosterPath() != null && !apiMovie.getPosterPath().isBlank())
                    .map(ApiMovie::toEntity)
                    .map(this::saveMovieIfNotExists)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        // merge both lists without duplicates
        List<Movie> mergedResults = mergeMovieLists(dbResults, apiResults);
        return sortByRelevance(mergedResults, query);
    }


    private List<Movie> mergeMovieLists(List<Movie> list1, List<Movie> list2) {
        return Stream.concat(list1.stream(), list2.stream())
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Movie::getId, m -> m, (m1, m2) -> m1),
                        map -> new ArrayList<>(map.values())
                ));
    }

    private List<Movie> sortByRelevance(List<Movie> movies, String query) {
        String lowerQuery = query.toLowerCase();

        return movies.stream()
                .sorted(Comparator.comparingInt((Movie m) -> relevanceScore(m, lowerQuery)).reversed())
                .collect(Collectors.toList());
    }

    private int relevanceScore(Movie movie, String query) {
        String title = movie.getTitle().toLowerCase();

        if (title.equals(query)) return 3;        // exact match
        if (title.startsWith(query)) return 2;    // starts with
        if (title.contains(query)) return 1;      // partial match
        return 0;                                 // weak match
    }

}