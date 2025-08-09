package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.PaintMatch;
import com.cinematch.cinematch.repository.DuluxColourRepository;
import com.cinematch.cinematch.repository.MovieRepository;
import com.cinematch.cinematch.repository.PaintMatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaintMatchService {

    private final MovieRepository movieRepository;
    private final PaintMatchRepository paintMatchRepository;
    private final DuluxColourRepository duluxColourRepository;

    public PaintMatchService(MovieRepository movieRepository,
                             PaintMatchRepository paintMatchRepository,
                             DuluxColourRepository duluxColourRepository) {
        this.movieRepository = movieRepository;
        this.paintMatchRepository = paintMatchRepository;
        this.duluxColourRepository = duluxColourRepository;
    }

    public List<DuluxColour> findMatchesByMovieId(Long movieId) {
        return paintMatchRepository.findById(movieId)
                .map(paintMatch -> {
                    // Map paint_1 ... paint_12 to DuluxColour objects
                    List<String> hexCodes = List.of(
                            paintMatch.getPaint_1(),
                            paintMatch.getPaint_2(),
                            paintMatch.getPaint_3(),
                            paintMatch.getPaint_4(),
                            paintMatch.getPaint_5(),
                            paintMatch.getPaint_6(),
                            paintMatch.getPaint_7(),
                            paintMatch.getPaint_8(),
                            paintMatch.getPaint_9(),
                            paintMatch.getPaint_10(),
                            paintMatch.getPaint_11(),
                            paintMatch.getPaint_12()
                    );

                    return hexCodes.stream()
                            .filter(Objects::nonNull)
                            .map(hex -> DuluxColour.builder().hexCode(hex).build())
                            .toList();
                })
                .orElse(List.of());
    }

    public List<DuluxColour> getOrCreatePaintMatches(Long movieId, List<DuluxColour> matches) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check if paint matches already exist for this movie
        PaintMatch existingMatches = paintMatchRepository.findByMovie(movie).orElse(null);

        if (existingMatches != null) {
            // Convert stored hex codes back to DuluxColour objects
            // You'll need to implement a method to get DuluxColour by hex code
            return convertHexCodesToDuluxColours(existingMatches);
        }

        // If no existing matches, save new ones
        savePaintMatches(movieId, matches);
        return matches;
    }

    public void savePaintMatches(Long movieId, List<DuluxColour> matches) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (paintMatchRepository.existsByMovie(movie)) {
            return; // Already exists, don't save again
        }

        PaintMatch paintMatch = PaintMatch.builder()
                .movie(movie)
                .paint_1(getHex(matches, 0))
                .paint_2(getHex(matches, 1))
                .paint_3(getHex(matches, 2))
                .paint_4(getHex(matches, 3))
                .paint_5(getHex(matches, 4))
                .paint_6(getHex(matches, 5))
                .paint_7(getHex(matches, 6))
                .paint_8(getHex(matches, 7))
                .paint_9(getHex(matches, 8))
                .paint_10(getHex(matches, 9))
                .paint_11(getHex(matches, 10))
                .paint_12(getHex(matches, 11))
                .build();

        paintMatchRepository.save(paintMatch);
    }

    private String getHex(List<DuluxColour> matches, int index) {
        return index < matches.size() ? matches.get(index).getHexCode() : null;
    }

    private List<DuluxColour> convertHexCodesToDuluxColours(PaintMatch paintMatch) {
        return List.of(
                        paintMatch.getPaint_1(),
                        paintMatch.getPaint_2(),
                        paintMatch.getPaint_3(),
                        paintMatch.getPaint_4(),
                        paintMatch.getPaint_5(),
                        paintMatch.getPaint_6(),
                        paintMatch.getPaint_7(),
                        paintMatch.getPaint_8(),
                        paintMatch.getPaint_9(),
                        paintMatch.getPaint_10(),
                        paintMatch.getPaint_11(),
                        paintMatch.getPaint_12()
                ).stream()
                .filter(hex -> hex != null)
                .map(hex -> duluxColourRepository.findByHexCode(hex).orElseGet(() -> {
                    DuluxColour colour = new DuluxColour();
                    colour.setHexCode(hex);
                    return colour; // fallback if not found in repository
                }))
                .collect(Collectors.toList());
    }
}
