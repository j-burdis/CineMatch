package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {

    private final MovieRepository movieRepository;
    private final ImageService imageService;

    public MovieRecommendationService(MovieRepository movieRepository, ImageService imageService) {
        this.movieRepository = movieRepository;
        this.imageService = imageService;
    }

//    main public method
//     Get movie recommendations based on colour similarity
//     @param currentMovieId - the movie to base recommendations on
//     @param limit - maximum number of recommendations
//     return list of recommended movies
    public List<Movie> getColourBasedRecommendations(Long currentMovieId, int limit) {
        Movie currentMovie = movieRepository.findById(currentMovieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (currentMovie.getDominantColour() == null) {
            return List.of();
        }

        int[] currentRGB = hexToRGB(currentMovie.getDominantColour());

        return movieRepository.findAll().stream()
                .filter(movie -> !movie.getId().equals(currentMovieId)) // Exclude current movie
                .filter(movie -> movie.getDominantColour() != null) // Only movies with dominant colours
                .map(movie -> new MovieWithSimilarity(movie, calculateColourSimilarity(currentRGB, hexToRGB(movie.getDominantColour()))))
                .sorted(Comparator.comparingDouble(MovieWithSimilarity::getSimilarity).reversed())
                .limit(limit)
                .map(MovieWithSimilarity::getMovie)
                .collect(Collectors.toList());
    }

//    private calculation method
//    calculate colour similarity using perceptual colour distance
//    returns value between 0 (very different) and 1 (identical)
    private double calculateColourSimilarity(int[] rgb1, int[] rgb2) {
        // use perceptual color distance
        double deltaR = rgb1[0] - rgb2[0];
        double deltaG = rgb1[1] - rgb2[1];
        double deltaB = rgb1[2] - rgb2[2];

        // weighted Euclidean distance (human eye sensitivity)
        double distance = Math.sqrt(2 * deltaR * deltaR + 4 * deltaG * deltaG + 3 * deltaB * deltaB);

        // convert to similarity (0-1 scale, where 1 is identical)
        // maximum possible distance is approximately 764 for weighted RGB
        double maxDistance = Math.sqrt(2 * 255 * 255 + 4 * 255 * 255 + 3 * 255 * 255);
        return 1.0 - (distance / maxDistance);
    }

//    private utility method
//    convert hex string to RGB array
    private int[] hexToRGB(String hex) {
        hex = hex.replace("#", "");
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color format: " + hex);
        }

        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);
        return new int[]{red, green, blue};
    }

//    helper class - store movie with its similarity score
    private static class MovieWithSimilarity {
        private final Movie movie;
        private final double similarity;

        public MovieWithSimilarity(Movie movie, double similarity) {
            this.movie = movie;
            this.similarity = similarity;
        }

        public Movie getMovie() { return movie; }
        public double getSimilarity() { return similarity; }
    }
}
