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


//     Get movie recommendations based on colour similarity
//     @param currentMovieId The movie to base recommendations on
//     @param limit Maximum number of recommendations to return
//     @return List of recommended movies

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

//    Get recommendations based on similar dominant colours only
//    @param currentMovieId The movie to base recommendations on
//    @param limit Maximum number of recommendations
//    @return List of recommended movies with similar colour palettes

    public List<Movie> getSimilarColourRecommendations(Long currentMovieId, int limit) {
        return getColourBasedRecommendations(currentMovieId, limit);
    }

//
//    Calculate colour similarity using perceptual colour distance
//    Returns a value between 0 (very different) and 1 (identical)
//
    private double calculateColourSimilarity(int[] rgb1, int[] rgb2) {
        // Use perceptual color distance (similar to ImageService)
        double deltaR = rgb1[0] - rgb2[0];
        double deltaG = rgb1[1] - rgb2[1];
        double deltaB = rgb1[2] - rgb2[2];

        // Weighted Euclidean distance (human eye sensitivity)
        double distance = Math.sqrt(2 * deltaR * deltaR + 4 * deltaG * deltaG + 3 * deltaB * deltaB);

        // Convert to similarity (0-1 scale, where 1 is identical)
        // Maximum possible distance is approximately 764 for weighted RGB
        double maxDistance = Math.sqrt(2 * 255 * 255 + 4 * 255 * 255 + 3 * 255 * 255);
        return 1.0 - (distance / maxDistance);
    }

//      Calculate complementary colour (opposite on colour wheel)
//      Not used in current implementation but kept for potential future use
    private int[] getComplementaryColour(int[] rgb) {
//      Simple complementary: invert each channel
        return new int[]{255 - rgb[0], 255 - rgb[1], 255 - rgb[2]};
    }

//    Convert hex string to RGB array

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

//    Helper class to store movie with its similarity score

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
