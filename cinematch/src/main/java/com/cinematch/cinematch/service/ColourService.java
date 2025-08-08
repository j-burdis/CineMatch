package com.cinematch.cinematch.service;

import com.cinematch.cinematch.DTO.ColourDTO;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.ColourRepository;
import com.cinematch.cinematch.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ColourService {
    //set up web client
    private final WebClient webClient;

    private final MovieRepository movieRepository;
    private final ColourRepository colourRepository;
    public ColourService(WebClient webClient, MovieRepository movieRepository, ColourRepository colourRepository) {
        this.webClient = webClient;
        this.movieRepository = movieRepository;
        this.colourRepository = colourRepository;
    }

    //api call to colour api
    public List<String> getColours(String hex) {
        ColourDTO resp = webClient.get()
                .uri("https://www.thecolorapi.com/scheme?hex=" + hex +"&mode=monochrome&count=12")
                .retrieve()
                .bodyToMono(ColourDTO.class)
                .block();

        //handle empty response, returning empty list
        if (resp == null || resp.getColors() == null) {
            return List.of(); // return empty list if null
        }

        //return list of strings
        return resp.getColors().stream()
                .map(c -> c.getHex().getValue())
                .collect(Collectors.toList());
    }

//    public void saveColours(Long movieId, List<String> coloursArray) {
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new RuntimeException("Movie not found"));
//
//        List<String> hexList = coloursArray.getColours().stream()
//                .map(colour -> colour.getHex().getClean())
//                .collect(Collectors.toList());
//
//        ColourModel model = ColourModel.builder()
//                .movie(movie)
//                .colour_1(getColour(hexList, 0))
//                .colour_2(getColour(hexList, 1))
//                .colour_3(getColour(hexList, 2))
//                .colour_4(getColour(hexList, 3))
//                .colour_5(getColour(hexList, 4))
//                .colour_6(getColour(hexList, 5))
//                .colour_7(getColour(hexList, 6))
//                .colour_8(getColour(hexList, 7))
//                .colour_9(getColour(hexList, 8))
//                .colour_10(getColour(hexList, 9))
//                .colour_11(getColour(hexList, 10))
//                .colour_12(getColour(hexList, 11))
//                .build();
//
//        colourRepository.save(model);
//    }

    public void saveColours(Long movieId, List<String> coloursArray) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        ColourModel model = ColourModel.builder()
                .movie(movie)
                .colour_1(getColour(coloursArray, 0))
                .colour_2(getColour(coloursArray, 1))
                .colour_3(getColour(coloursArray, 2))
                .colour_4(getColour(coloursArray, 3))
                .colour_5(getColour(coloursArray, 4))
                .colour_6(getColour(coloursArray, 5))
                .colour_7(getColour(coloursArray, 6))
                .colour_8(getColour(coloursArray, 7))
                .colour_9(getColour(coloursArray, 8))
                .colour_10(getColour(coloursArray, 9))
                .colour_11(getColour(coloursArray, 10))
                .colour_12(getColour(coloursArray, 11))
                .build();

        colourRepository.save(model);
    }

    private String getColour(List<String> colours, int index) {
        return index < colours.size() ? colours.get(index) : null;
    }



}
