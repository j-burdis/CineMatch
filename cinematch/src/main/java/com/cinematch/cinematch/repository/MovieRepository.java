package com.cinematch.cinematch.repository;

import com.cinematch.cinematch.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Find movie by title and release date to avoid duplicates
    @Query("SELECT m FROM Movie m WHERE m.title = :title AND " +
            "(m.releaseDate = :releaseDate OR (m.releaseDate IS NULL AND :releaseDate IS NULL))")
    Optional<Movie> findByTitleAndReleaseDate(@Param("title") String title,
                                              @Param("releaseDate") java.time.LocalDate releaseDate);

    Optional<Movie> findByTitle(String title);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> searchByTitle(@Param("query") String query);

    List<Movie> findByCategory(String category);
}
