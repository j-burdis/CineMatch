package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDetailsService movieDetailsService;


    @GetMapping
    public String getPopularMovies(Model model) {
        model.addAttribute("movies", movieService.getMoviesByCategory("popular"));
        return "movies";
    }

    // Search movies
    @GetMapping("/search")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        model.addAttribute("movies", movieService.searchMovies(query));
        return "movies";
    }

    // Get movies by category dynamically (popular, top_rated, upcoming)
    @GetMapping("/{category:popular|top_rated|upcoming}")
    public String getMoviesByCategory(@PathVariable String category, Model model) {
        model.addAttribute("movies", movieService.getMoviesByCategory(category));
        return "movies";
    }

    @GetMapping("/{id:\\d+}")
    public ModelAndView getMovieDetails(@PathVariable Long id) {
        return movieDetailsService.buildMovieDetail(id);
    }




    // Admin endpoint to refresh a category from API
//    @PostMapping("/refresh/{category}")
//    public String refreshCategory(@PathVariable String category, Model model) {
//        model.addAttribute("movies", movieService.refreshMoviesFromApi(category));
//        return "fragments/movie-list :: movieList";
//    }
}
