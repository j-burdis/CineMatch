package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.ColourModel;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.PaintMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ColourRepository extends JpaRepository<ColourModel, Long> {
    Optional<ColourModel> findByMovie(Movie movie);
    boolean existsByMovie(Movie movie);
}
