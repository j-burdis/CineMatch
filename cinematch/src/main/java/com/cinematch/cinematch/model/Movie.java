package com.cinematch.cinematch.model;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

//@Entity
@Getter
@Setter
public class Movie {
    private String title;
    private String poster_path;
}