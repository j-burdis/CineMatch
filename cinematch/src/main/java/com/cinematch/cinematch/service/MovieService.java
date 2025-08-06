package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.MovieResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MovieService {

    @Value("${tmdb.api.key}")
    private String apiKey;

    public List<Movie> getPopularMovies() {
        String URL = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        MovieResponse response = restTemplate.getForObject(URL, MovieResponse.class);
        return response != null ? response.getResults() : List.of();
    }
}