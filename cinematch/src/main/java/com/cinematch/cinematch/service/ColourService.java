package com.cinematch.cinematch.service;

import com.cinematch.cinematch.DTO.ColourDTO;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.repository.ColourRepository;
import com.cinematch.cinematch.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ColourService {
    //set up web client
    private final WebClient webClient;
    private final MovieRepository movieRepository;
    private final ColourRepository colourRepository;
    private final ImageService imageService;

    public ColourService(WebClient webClient, MovieRepository movieRepository,
                         ColourRepository colourRepository, ImageService imageService) {
        this.webClient = webClient;
        this.movieRepository = movieRepository;
        this.colourRepository = colourRepository;
        this.imageService = imageService;
    }

    public List<String> getOrCreateColours(Long movieId, String hex) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // check if colours exist for this movie
        Optional<ColourModel> existingColoursOpt = colourRepository.findByMovie(movie);

        if (existingColoursOpt.isPresent()) {
            ColourModel existingColours = existingColoursOpt.get();
            // return existing colours as a list
            return List.of(
                    existingColours.getColour_1(), existingColours.getColour_2(),
                    existingColours.getColour_3(), existingColours.getColour_4(),
                    existingColours.getColour_5(), existingColours.getColour_6(),
                    existingColours.getColour_7(), existingColours.getColour_8(),
                    existingColours.getColour_9(), existingColours.getColour_10(),
                    existingColours.getColour_11(), existingColours.getColour_12()
            ).stream().filter(Objects::nonNull).collect(Collectors.toList());
        }

        // if no existing colours, fetch from API and save
        // generate dominant palette
        List<String> dominantPalette = getColours(hex);

        // generate secondary palette
        String secondaryHex = imageService.getSecondaryColour(
                movie.getPosterUrl(),
                "#" + hex // keep '#' for ImageService method
        ).replace("#", "");
        List<String> secondaryPalette = getColours(secondaryHex);

        // Save both palettes
        saveColours(movieId, dominantPalette, secondaryPalette);

        return dominantPalette;
    }

    public List<String> findColoursByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return colourRepository.findByMovie(movie)
                .map(colourModel -> List.of(
                        colourModel.getColour_1(), colourModel.getColour_2(),
                        colourModel.getColour_3(), colourModel.getColour_4(),
                        colourModel.getColour_5(), colourModel.getColour_6(),
                        colourModel.getColour_7(), colourModel.getColour_8(),
                        colourModel.getColour_9(), colourModel.getColour_10(),
                        colourModel.getColour_11(), colourModel.getColour_12()
                ).stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .orElse(List.of()); // return empty list if none
    }

    public List<String> findSecondaryColoursByMovieId(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return colourRepository.findByMovie(movie)
                .map(colourModel -> List.of(
                        colourModel.getSecondary_colour_1(), colourModel.getSecondary_colour_2(),
                        colourModel.getSecondary_colour_3(), colourModel.getSecondary_colour_4(),
                        colourModel.getSecondary_colour_5(), colourModel.getSecondary_colour_6(),
                        colourModel.getSecondary_colour_7(), colourModel.getSecondary_colour_8(),
                        colourModel.getSecondary_colour_9(), colourModel.getSecondary_colour_10(),
                        colourModel.getSecondary_colour_11(), colourModel.getSecondary_colour_12()
                ).stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .orElse(List.of());
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
            // return empty list if null
            return List.of();
        }

        //return list of strings
        return resp.getColors().stream()
                .map(c -> c.getHex().getValue())
                .collect(Collectors.toList());
    }

    public void saveColours(Long movieId, List<String> dominantColours,  List<String> secondaryColours) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (colourRepository.existsByMovie(movie)) {
            return; // already exists, don't save again
        }

        ColourModel model = ColourModel.builder()
                .movie(movie)
                .colour_1(getColour(dominantColours, 0))
                .colour_2(getColour(dominantColours, 1))
                .colour_3(getColour(dominantColours, 2))
                .colour_4(getColour(dominantColours, 3))
                .colour_5(getColour(dominantColours, 4))
                .colour_6(getColour(dominantColours, 5))
                .colour_7(getColour(dominantColours, 6))
                .colour_8(getColour(dominantColours, 7))
                .colour_9(getColour(dominantColours, 8))
                .colour_10(getColour(dominantColours, 9))
                .colour_11(getColour(dominantColours, 10))
                .colour_12(getColour(dominantColours, 11))

                .secondary_colour_1(getColour(secondaryColours, 0))
                .secondary_colour_2(getColour(secondaryColours, 1))
                .secondary_colour_3(getColour(secondaryColours, 2))
                .secondary_colour_4(getColour(secondaryColours, 3))
                .secondary_colour_5(getColour(secondaryColours, 4))
                .secondary_colour_6(getColour(secondaryColours, 5))
                .secondary_colour_7(getColour(secondaryColours, 6))
                .secondary_colour_8(getColour(secondaryColours, 7))
                .secondary_colour_9(getColour(secondaryColours, 8))
                .secondary_colour_10(getColour(secondaryColours, 9))
                .secondary_colour_11(getColour(secondaryColours, 10))
                .secondary_colour_12(getColour(secondaryColours, 11))
                .build();

        colourRepository.save(model);
    }

    private String getColour(List<String> colours, int index) {
        return index < colours.size() ? colours.get(index) : null;
    }
}
