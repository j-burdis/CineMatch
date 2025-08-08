package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.PaintMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaintMatchRepository extends JpaRepository<PaintMatch, Long> {
    Optional<PaintMatch> findByMovie(Movie movie);
    boolean existsByMovie(Movie movie);
}
