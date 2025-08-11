package com.cinematch.cinematch.controller;

import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@Import(GlobalExceptionHandler.class)

public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

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
                        Matchers.hasProperty("posterUrl", Matchers.is("https://image.tmdb.org/t/p/w200Test Image URL")),
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

    //    **** error-handling ****
    @Test
    public void shouldHandleServiceException() throws Exception {
        Mockito.when(movieService.getPopularMovies())
                .thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/movies"))
                .andExpect(status().is5xxServerError())
                .andExpect(view().name("error/500"));
    }
}
