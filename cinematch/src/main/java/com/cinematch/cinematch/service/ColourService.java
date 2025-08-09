package com.cinematch.cinematch.service;

import com.cinematch.cinematch.DTO.ColourDTO;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.ColourRepository;
import com.cinematch.cinematch.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
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

    public List<String> findColoursByMovieId(Long movieId) {
        return colourRepository.findById(movieId)
                .flatMap(colourModel -> Optional.of(List.of(
                        colourModel.getColour_1(),
                        colourModel.getColour_2(),
                        colourModel.getColour_3(),
                        colourModel.getColour_4(),
                        colourModel.getColour_5(),
                        colourModel.getColour_6(),
                        colourModel.getColour_7(),
                        colourModel.getColour_8(),
                        colourModel.getColour_9(),
                        colourModel.getColour_10(),
                        colourModel.getColour_11(),
                        colourModel.getColour_12()
                )))
                .orElse(List.of()); // return empty list if none
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

    public List<String> getOrCreateColours(Long movieId, String hex) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check if colours already exist for this movie
        Optional<ColourModel> existingColoursOpt = colourRepository.findByMovie(movie);

        if (existingColoursOpt.isPresent()) {
            ColourModel existingColours = existingColoursOpt.get();
            // Return existing colours as a list
            return List.of(
                    existingColours.getColour_1(), existingColours.getColour_2(),
                    existingColours.getColour_3(), existingColours.getColour_4(),
                    existingColours.getColour_5(), existingColours.getColour_6(),
                    existingColours.getColour_7(), existingColours.getColour_8(),
                    existingColours.getColour_9(), existingColours.getColour_10(),
                    existingColours.getColour_11(), existingColours.getColour_12()
            ).stream().filter(colour -> colour != null).collect(Collectors.toList());
        }

        // If no existing colours, fetch from API and save
        List<String> coloursArray = getColours(hex);
        saveColours(movieId, coloursArray);
        return coloursArray;
    }

    public void saveColours(Long movieId, List<String> coloursArray) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (colourRepository.existsByMovie(movie)) {
            return; // Already exists, don't save again
        }

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
