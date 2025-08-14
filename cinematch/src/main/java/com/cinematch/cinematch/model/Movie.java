package com.cinematch.cinematch.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private Long id;
//    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(nullable = false)
    private String title;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "dominant_colour")
    private String dominantColour;

    @Column(name = "category")
    private String category;

    // Additional field for API response mapping
    @Transient
    private String poster_path;

    @Transient
    private String release_date;

    public Movie(Long id, String title, String posterPath, String releaseDate, String dominantColour) {
        this.id = id;
        this.title = title;
        this.poster_path = posterPath;
        this.release_date = releaseDate;
        this.dominantColour = dominantColour;

        this.posterUrl = posterPath != null ? "https://image.tmdb.org/t/p/w780" + posterPath : null;

        if (releaseDate != null && !releaseDate.isEmpty()) {
            try {
                this.releaseDate = LocalDate.parse(releaseDate);
            } catch (Exception e) {
                this.releaseDate = null;
            }
        }
    }
}