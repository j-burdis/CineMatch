package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.MovieService;
import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.service.ImageService;
import com.cinematch.cinematch.service.ColourService;
import com.cinematch.cinematch.service.PaletteToDuluxService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//Controller for colour API call route
@Controller
public class ColourController {

    private final ColourService colourService;
    private final ImageService imageService;
    private final MovieService movieService;
    private final PaletteToDuluxService paletteToDuluxService;

    public ColourController(ColourService colourService, ImageService imageService, MovieService movieService, PaletteToDuluxService paletteToDuluxService) {
        this.colourService = colourService;
        this.imageService = imageService;
        this.movieService = movieService;
        this.paletteToDuluxService = paletteToDuluxService;
    }

    @GetMapping("/image-colour")
    public String runImageTest(@RequestParam("url") String imageUrl,
                               @RequestParam("movieId") Long movieId) {

        String hexCode = imageService.getDominantColour(imageUrl);

        hexCode = hexCode.replace("#", "");
//        return "redirect:/colours/" + hexCode + "?movieId=" + movieId;
        return "redirect:/colours/" + movieId + "/" + hexCode;

    }


    //route for colour api request
    @GetMapping("/colours/{movieId}/{hex}")
    public ModelAndView ColourPalette(@PathVariable Long movieId, @PathVariable String hex) {
//
        List<String> ColoursArray = colourService.getColours(hex);
        colourService.saveColours(movieId, ColoursArray);
        List<DuluxColour> closestMatches = paletteToDuluxService.getClosestPaintMatches(ColoursArray);


        //thymeleaf connection
        ModelAndView modelAndView = new ModelAndView("colour-palette");
        modelAndView.addObject("coloursArray", ColoursArray);
        modelAndView.addObject("closestMatches", closestMatches);
        System.out.println(modelAndView);
        return modelAndView;

    }
}



