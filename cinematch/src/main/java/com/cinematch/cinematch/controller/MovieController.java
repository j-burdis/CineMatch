package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.service.MovieDetailsService;
import com.cinematch.cinematch.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieDetailsService movieDetailsService;

    @GetMapping("/movies")
    public String showMovies(Model model) {
        model.addAttribute("movies", movieService.getPopularMovies());
        return "movies";
    }

    @GetMapping("/movies/{id}")
    public ModelAndView showMovieDetails(@PathVariable Long id) {
        return movieDetailsService.buildMovieDetail(id);
    }

    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam("q") String query, Model model) {
        model.addAttribute("movies", movieService.searchMovies(query));
        return "movies";
    }

}
