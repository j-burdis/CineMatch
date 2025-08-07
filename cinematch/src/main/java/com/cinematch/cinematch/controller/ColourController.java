package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.service.ImageService;
import com.cinematch.cinematch.service.ColourService;
import com.cinematch.cinematch.service.PalleteToDuluxService;
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
    private final PalleteToDuluxService palleteToDuluxService;

    public ColourController(ColourService colourService, ImageService imageService, PalleteToDuluxService palleteToDuluxService) {
        this.colourService = colourService;
        this.imageService = imageService;
        this.palleteToDuluxService = palleteToDuluxService;
    }

//    TODO: edit once able to pass url into runImageTest()
    @GetMapping("/image-colour")
        public String runImageTest(@RequestParam("url") String imageUrl) {
            String hexCode = imageService.getDominantColour(imageUrl);
            return "redirect:/colours/" + hexCode;
    }

    //route for colour api request
    @GetMapping("/colours/{hex}")
    public ModelAndView ColourPalette(@PathVariable String hex) {

        List<String> ColoursArray = colourService.getColours(hex);
        List<DuluxColour> closestMatches = palleteToDuluxService.getClosestPaintMatches(ColoursArray);


        //thymeleaf connection
        ModelAndView modelAndView = new ModelAndView("colour-palette");
        modelAndView.addObject("coloursArray", ColoursArray);
        modelAndView.addObject("closestMatches", closestMatches);
        System.out.println(modelAndView);
        return modelAndView;

    }
}