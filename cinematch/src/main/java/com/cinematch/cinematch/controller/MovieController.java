package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

//    @GetMapping("/movies")
//    public String showMovies(Model model) {
//        model.addAttribute("movies", movieService.getPopularMovies());
//        return "movies";
//    }

    @GetMapping("/movies")
    public String showMovies(Model model) {
        List<Movie> movies = movieService.getPopularMovies();
        model.addAttribute("movies", movies != null ? movies : Collections.emptyList());
        return "movies";
    }


}

