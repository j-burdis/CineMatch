package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Find movie by title and release date to avoid duplicates
    @Query("SELECT m FROM Movie m WHERE m.title = :title AND " +
            "(m.releaseDate = :releaseDate OR (m.releaseDate IS NULL AND :releaseDate IS NULL))")
    Optional<Movie> findByTitleAndReleaseDate(@Param("title") String title,
                                              @Param("releaseDate") java.time.LocalDate releaseDate);

    // Alternative method - find by title only if you prefer simpler duplicate checking
    Optional<Movie> findByTitle(String title);
}
