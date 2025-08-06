package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    public String showMovies(Model model) {
        model.addAttribute("movies", movieService.getPopularMovies());
        return "movies";
    }


}

