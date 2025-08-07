package com.cinematch.cinematch.model;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
public class ApiMovie {
    private Long id;

    private String title;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("release_date")
    private String releaseDate;

    // Convert to entity
    public Movie toEntity() {
        return new Movie(this. id, this.title, this.posterPath, this.releaseDate);
    }
}