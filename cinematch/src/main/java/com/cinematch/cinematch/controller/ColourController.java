package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.ImageService;
import com.cinematch.cinematch.service.ColourService;
import com.cinematch.cinematch.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//Controller for colour API call route
@Controller
public class ColourController {

    private final ColourService colourService;
    private final ImageService imageService;
    private final MovieService movieService;

    public ColourController(ColourService colourService, ImageService imageService, MovieService movieService) {
        this.colourService = colourService;
        this.imageService = imageService;
        this.movieService = movieService;
    }

//    @GetMapping("/image-colour")
//        public String runImageTest(@RequestParam("url") String imageUrl) {
//            String hexCode = imageService.getDominantColour(imageUrl);
//            return "redirect:/colours/" + hexCode;
//    }

    @GetMapping("/image-colour")
    public String runImageTest(@RequestParam("url") String imageUrl, @RequestParam("movieId") Long movieId) {

        String hexCode = imageService.getDominantColour(imageUrl);

        hexCode = hexCode.replace("#", "");
        return "redirect:/colours/" + hexCode + "?movieId=" + movieId;
    }


    //route for colour api request
    @GetMapping("/colours/{hex}")
    public ModelAndView ColourPalette(@PathVariable String hex, @RequestParam Long movieId) {
        List<String> ColoursArray = colourService.getColours(hex);
        colourService.saveColours(movieId, ColoursArray);
        //thymeleaf connection
        ModelAndView modelAndView = new ModelAndView("colour-palette");
        modelAndView.addObject("coloursArray", ColoursArray);

        //TODO added below for test - added findById to MovieService
//        Movie movie = movieService.findById(movieId); // fetch movie from DB
//        modelAndView.addObject("movie", movie);
        return modelAndView;

    }
}



