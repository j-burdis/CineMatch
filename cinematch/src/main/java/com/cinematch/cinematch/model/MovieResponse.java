package com.cinematch.cinematch.model;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//@Entity
@Getter
@Setter
public class MovieResponse {
    private List<Movie> results;

    @Getter
    @Setter
    public static class ApiMovie {
        private String title;

        @JsonProperty("poster_path")
        private String posterPath;

        @JsonProperty("release_date")
        private String releaseDate;

        // Convert to entity
        public Movie toEntity() {
            return new Movie(this.title, this.posterPath, this.releaseDate);
        }
    }
}