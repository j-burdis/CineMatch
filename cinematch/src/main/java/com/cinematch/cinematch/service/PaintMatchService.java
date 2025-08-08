package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.DuluxColour;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.PaintMatch;
import com.cinematch.cinematch.repository.MovieRepository;
import com.cinematch.cinematch.repository.PaintMatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaintMatchService {

    private final MovieRepository movieRepository;
    private final PaintMatchRepository paintMatchRepository;

    public PaintMatchService(MovieRepository movieRepository, PaintMatchRepository paintMatchRepository) {
        this.movieRepository = movieRepository;
        this.paintMatchRepository = paintMatchRepository;
    }

    public void savePaintMatches(Long movieId, List<DuluxColour> matches) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

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
}
