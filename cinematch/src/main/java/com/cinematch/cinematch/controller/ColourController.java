package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.service.ColourService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Controller for colour API call route
@RestController
public class ColourController {

    private final ColourService colourService;

    public ColourController(ColourService colourService) {
        this.colourService = colourService;
    }

    @GetMapping("/colours")
    public List<String> ColourPalette(@PathVariable String hex) {
        return colourService.getColours(hex);
    }
}