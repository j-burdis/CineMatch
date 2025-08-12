package com.cinematch.cinematch.controllers;

import com.cinematch.cinematch.controller.MovieController;
import com.cinematch.cinematch.exception.MovieNotFoundException;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.MovieDetailsService;
import com.cinematch.cinematch.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)

public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieDetailsService movieDetailsService;

    @Autowired
    private MovieController movieController;

    @Test
    public void shouldLoadMoviesPageWithCorrectModelAndView() throws Exception {
        Mockito.when(movieService.getPopularMovies()).thenReturn(List.of());
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("movies"));
    }

    @Test
    public void shouldReturnMoviesList() throws Exception {
        List<Movie> mockMovies = List.of(new Movie(1L, "Test Movie", "Test Image URL", "Test Release Date", "Test Dom Colour"));
        Mockito.when(movieService.getPopularMovies()).thenReturn(mockMovies);
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(1)));
    }

    @Test
    public void shouldCallGetPopularMoviesOnce() throws Exception {
        Mockito.when(movieService.getPopularMovies()).thenReturn(List.of());

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk());

        Mockito.verify(movieService, Mockito.times(1)).getPopularMovies();
        //getPopularMovies() called exactly x 1 during test
    }

    @Test
    public void shouldPassCorrectMovieDataToView() throws Exception {
        List<Movie> mockMovies = List.of(
                new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Dominant Colour")
        );

        Mockito.when(movieService.getPopularMovies()).thenReturn(mockMovies);

        MvcResult result = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        List<Movie> movies = (List<Movie>) result.getModelAndView().getModel().get("movies");

        assertThat(movies, Matchers.hasItem(
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.is(1L)),
                        Matchers.hasProperty("title", Matchers.is("Test Movie")),
                        Matchers.hasProperty("posterUrl", Matchers.is("https://image.tmdb.org/t/p/w780Test Image URL")),
                        Matchers.hasProperty("releaseDate", Matchers.is(LocalDate.parse("2025-01-01")))
                )
        ));
    }

    @Test
    public void shouldRenderMoviesWhenMissingPosterUrl() throws Exception {
        Movie movieWithNoPoster = new Movie(1L, "No Poster Movie", null, "2025-01-01", "Dominant Colour");

        Mockito.when(movieService.getPopularMovies()).thenReturn(List.of(movieWithNoPoster));

        MvcResult result = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        List<Movie> movies = (List<Movie>) result.getModelAndView().getModel().get("movies");

        // Assert movie exists and posterUrl is null
        assertThat(movies, Matchers.hasItem(
                Matchers.allOf(
                        Matchers.hasProperty("id", Matchers.is(1L)),
                        Matchers.hasProperty("title", Matchers.is("No Poster Movie")),
                        Matchers.hasProperty("posterUrl", Matchers.nullValue()),
                        Matchers.hasProperty("releaseDate", Matchers.is(LocalDate.parse("2025-01-01")))
                )
        ));
    }
    //checks controller and view can handle null w/o crashing
    @Test
    public void shouldHandleNullMoviesListGracefully() throws Exception {
        Mockito.when(movieService.getPopularMovies()).thenReturn(null);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("movies"));
    }

    //simulates HTTP request, tests flow (routing, controller, view rendering, model
    @Test
    void shouldReturnMovieDetails() throws Exception {
        Long movieId = 1L;

        ModelAndView mockView = new ModelAndView("colour-palette");
        mockView.addObject("movieDetail", new Movie(movieId, "Test Movie", "url", "2025-01-01", "Dominant Colour"));
        Mockito.when(movieDetailsService.buildMovieDetail(movieId)).thenReturn(mockView);

        mockMvc.perform(get("/movies/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(view().name("colour-palette"))
                .andExpect(model().attributeExists("movieDetail"))
                .andExpect(model().attribute("movieDetail", Matchers.allOf(
                        Matchers.hasProperty("title", Matchers.is("Test Movie")),
                        Matchers.hasProperty("posterUrl", Matchers.is("https://image.tmdb.org/t/p/w780url")),
                        Matchers.hasProperty("releaseDate", Matchers.is(LocalDate.parse("2025-01-01"))),
                        Matchers.hasProperty("dominantColour", Matchers.is("Dominant Colour"))
                )));
    }

    //calls controller directly - tests only logic and interactions with mocked service
    @Test
    void showMovieDetails_shouldCallServiceAndReturnModelAndView() {
        Long movieId = 1L;
        ModelAndView expected = new ModelAndView("colour-palette");
        expected.addObject("movieDetail", new Movie(movieId, "Test Movie", "url", "2025-01-01", "Dominant Colour"));

        Mockito.when(movieDetailsService.buildMovieDetail(movieId)).thenReturn(expected);

        ModelAndView actual = movieController.showMovieDetails(movieId);

        Mockito.verify(movieDetailsService).buildMovieDetail(movieId);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldHandleServiceException() throws Exception {
        Mockito.when(movieService.getPopularMovies())
                .thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/movies"))
                .andExpect(status().is5xxServerError())
                .andExpect(view().name("error/500"));
    }

    @Test
    void shouldReturn404WhenMovieNotFound() throws Exception {
        Long movieId = 1L;

        Mockito.when(movieDetailsService.buildMovieDetail(movieId))
                .thenThrow(new MovieNotFoundException("Movie not found"));

        mockMvc.perform(get("/movies/{id}", movieId))
                .andExpect(status().isNotFound())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", "Movie not found"));

    }
}