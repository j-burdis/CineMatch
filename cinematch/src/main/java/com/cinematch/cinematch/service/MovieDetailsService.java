package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.model.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
public class MovieDetailsService {

    private final ColourService colourService;
    private final PaletteToDuluxService paletteToDuluxService;
    private final PaintMatchService paintMatchService;
    private final MovieService movieService;
    private final ImageService imageService;
    private final MovieRecommendationService movieRecommendationService;

    public MovieDetailsService(
            ColourService colourService,
            PaletteToDuluxService paletteToDuluxService,
            PaintMatchService paintMatchService,
            MovieService movieService,
            ImageService imageService,
            MovieRecommendationService movieRecommendationService) {
        this.colourService = colourService;
        this.paletteToDuluxService = paletteToDuluxService;
        this.paintMatchService = paintMatchService;
        this.movieService = movieService;
        this.imageService = imageService;
        this.movieRecommendationService = movieRecommendationService;
    }

    public ModelAndView buildMovieDetail(Long movieId) {

        // Try to load existing colours
        List<String> dominantPalette = colourService.findColoursByMovieId(movieId);
        List<String> secondaryPalette = colourService.findSecondaryColoursByMovieId(movieId);

        // If none found, generate them from poster image
        if (dominantPalette.isEmpty() || secondaryPalette.isEmpty()) {
            Movie movie = movieService.findById(movieId);

            String dominantHex = imageService.getDominantColour(movie.getPosterUrl()).replace("#", "");
            dominantPalette = colourService.getColours(dominantHex);

            String secondaryHex = imageService.getSecondaryColour(movie.getPosterUrl(), dominantHex).replace("#", "");
            secondaryPalette = colourService.getColours(secondaryHex);

            colourService.saveColours(movieId, dominantPalette, secondaryPalette);
        }

        // Try to load existing matches
        List<DuluxColour> finalMatches = paintMatchService.findMatchesByMovieId(movieId);
        // If none found, generate from colours
        if (finalMatches.isEmpty()) {
            List<DuluxColour> closestMatches = paletteToDuluxService.getClosestPaintMatches(dominantPalette);
            finalMatches = paintMatchService.getOrCreatePaintMatches(movieId, closestMatches);
        }

        List<DuluxColour> finalSecondaryMatches = paintMatchService.findSecondaryMatchesByMovieId(movieId);
        if (finalSecondaryMatches.isEmpty()) {
            List<DuluxColour> closestSecondaryMatches = paletteToDuluxService.getClosestPaintMatches(secondaryPalette);
            finalSecondaryMatches = paintMatchService.getOrCreateSecondaryPaintMatches(movieId, closestSecondaryMatches);
        }

        Movie movie = movieService.findById(movieId);

        List<Movie> recommendedMovies = movieRecommendationService.getSimilarColourRecommendations(movieId, 6);

        ModelAndView mav = new ModelAndView("colour-palette");
        mav.addObject("coloursArray", dominantPalette);
        mav.addObject("secondaryColours", secondaryPalette);
        mav.addObject("closestMatches", finalMatches);
        mav.addObject("closestSecondaryMatches", finalSecondaryMatches);
        mav.addObject("posterUrl", movie.getPosterUrl());
        mav.addObject("title", movie.getTitle());

        mav.addObject("movieDetail", movie);

        mav.addObject("recommendedMovies", recommendedMovies);

        return mav;
    }
}

