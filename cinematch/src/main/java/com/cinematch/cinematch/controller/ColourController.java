package com.cinematch.cinematch.controller;

import ch.qos.logback.core.model.Model;
import com.cinematch.cinematch.service.ColourService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//Controller for colour API call route
@RestController
public class ColourController {

    private final ColourService colourService;

    public ColourController(ColourService colourService) {
        this.colourService = colourService;
    }

    //route for colour api request
    @GetMapping("/colours/{hex}")
    public ModelAndView ColourPalette(@PathVariable String hex) {
        List<String> ColoursArray = colourService.getColours(hex);

        //thymeleaf connection
        ModelAndView modelAndView = new ModelAndView("colour-palette");
        modelAndView.addObject("coloursArray", ColoursArray);
        System.out.println(modelAndView);
        return modelAndView;

    }
}