package com.cinematch.cinematch.model;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Entity
@Getter
@Setter
public class MovieResponse {
    private List<Movie> results;
}