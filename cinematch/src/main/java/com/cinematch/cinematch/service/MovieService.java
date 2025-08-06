package com.cinematch.cinematch.service;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.model.MovieResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MovieService {

    private final String apiKey;

    public MovieService() {
        Dotenv dotenv = Dotenv.load();
        this.apiKey = dotenv.get("TMDB_API_KEY");
    }

    public List<Movie> getPopularMovies() {
        String url = "https://api.themoviedb.org/3/movie/popular?api_key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        MovieResponse response = restTemplate.getForObject(url, MovieResponse.class);
        return response != null ? response.getResults() : List.of();
    }
}