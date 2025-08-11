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

    public MovieDetailsService(
            ColourService colourService,
            PaletteToDuluxService paletteToDuluxService,
            PaintMatchService paintMatchService,
            MovieService movieService,
            ImageService imageService) {
        this.colourService = colourService;
        this.paletteToDuluxService = paletteToDuluxService;
        this.paintMatchService = paintMatchService;
        this.movieService = movieService;
        this.imageService = imageService;
    }

    public ModelAndView buildMovieDetail(Long movieId) {

        // Try to load existing colours
        List<String> coloursArray = colourService.findColoursByMovieId(movieId);

        // If none found, generate them from poster image
        if (coloursArray.isEmpty()) {
            Movie movie = movieService.findById(movieId);
            String hexCode = imageService.getDominantColour(movie.getPosterUrl()).replace("#", "");
            coloursArray = colourService.getOrCreateColours(movieId, hexCode);
        }

        // Try to load existing matches
        List<DuluxColour> finalMatches = paintMatchService.findMatchesByMovieId(movieId);

        // If none found, generate from colours
        if (finalMatches.isEmpty()) {
            List<DuluxColour> closestMatches = paletteToDuluxService.getClosestPaintMatches(coloursArray);
            finalMatches = paintMatchService.getOrCreatePaintMatches(movieId, closestMatches);
        }

        Movie movie = movieService.findById(movieId);

        ModelAndView mav = new ModelAndView("colour-palette");
        mav.addObject("coloursArray", coloursArray);
        mav.addObject("closestMatches", finalMatches);
        mav.addObject("posterUrl", movie.getPosterUrl());
        mav.addObject("title", movie.getTitle());

        mav.addObject("movieDetail", movie);

        return mav;
    }
}

