//package com.cinematch.cinematch.controller;
//
//import com.cinematch.cinematch.model.Movie;
//import com.cinematch.cinematch.service.*;
//import com.cinematch.cinematch.model.DuluxColour;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//@Controller
//public class ColourController {
//
//    private final ColourService colourService;
//    private final ImageService imageService;
//    private final MovieService movieService;
//    private final PaletteToDuluxService paletteToDuluxService;
//    private final PaintMatchService paintMatchService;
//
//    public ColourController(ColourService colourService,
//                            ImageService imageService,
//                            MovieService movieService,
//                            PaletteToDuluxService paletteToDuluxService,
//                            PaintMatchService paintMatchService) {
//        this.colourService = colourService;
//        this.imageService = imageService;
//        this.movieService = movieService;
//        this.paletteToDuluxService = paletteToDuluxService;
//        this.paintMatchService = paintMatchService;
//    }
//
//    @GetMapping("/image-colour")
//    public String runImageTest(@RequestParam("url") String imageUrl,
//                               @RequestParam("movieId") Long movieId) {
//
//        String hexCode = imageService.getDominantColour(imageUrl);
//        hexCode = hexCode.replace("#", "");
//        return "redirect:/colours/" + movieId + "/" + hexCode;
//
//    }
//
//    @GetMapping("/colours/{movieId}/{hex}")
//    public ModelAndView ColourPalette(@PathVariable Long movieId, @PathVariable String hex) {
//
//        List<String> ColoursArray = colourService.getOrCreateColours(movieId, hex);
//
//        List<DuluxColour> closestMatches = paletteToDuluxService.getClosestPaintMatches(ColoursArray);
//        List<DuluxColour> finalMatches = paintMatchService.getOrCreatePaintMatches(movieId, closestMatches);
//
////        get movie data
//        Movie movie = movieService.findById(movieId);
//
//        //thymeleaf connection
//        ModelAndView modelAndView = new ModelAndView("show-movie");
//        modelAndView.addObject("coloursArray", ColoursArray);
////        modelAndView.addObject("closestMatches", closestMatches);
//        modelAndView.addObject("closestMatches", finalMatches);
//        modelAndView.addObject("posterUrl", movie.getPosterUrl());
//        modelAndView.addObject("title", movie.getTitle());
//        return modelAndView;
//    }
//}
