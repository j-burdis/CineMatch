package com.cinematch.cinematch.controllers;

import com.cinematch.cinematch.controller.MovieController;
import com.cinematch.cinematch.model.Movie;
import com.cinematch.cinematch.service.MovieDetailsService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

//    *** redirectToMovies() ***
    @Test
    public void MovieController_RedirectToMovies_ReturnsRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/movies"));
    }
//    *** getAllMovies() ***
    @Test
    public void MovieController_GetAllMovies_ReturnsEmptyListWhenNoMoviesInDatabase() throws Exception {
        Mockito.when(movieService.getAllMoviesFromDatabase()).thenReturn(List.of());
        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(List.of());

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.empty()))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_GetAllMovies_LoadsMoviesPageWithCorrectModelAndView() throws Exception {
        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(List.of());
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("movies"))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_GetAllMovies_ReturnsSingleMovie() throws Exception {
        List<Movie> mockMovies = List.of(new Movie(1L, "Test Movie", "Test Image URL", "Test Release Date", "Test Dom Colour"));
        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(mockMovies);
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(1)));
    }

    @Test
    public void MovieController_GetAllMovies_ReturnsMoviesList() throws Exception {
        List<Movie> mockMovies = List.of(
                new Movie(1L, "Test Movie 1", "Test Image URL 1", "Test Release Date 1", "Test Dom Colour"),
                new Movie(2L, "Test Movie 2", "Test Image URL 2", "Test Release Date 2", "Test Dom Colour 2"),
                new Movie(3L, "Test Movie 3", "Test Image URL 3", "Test Release Date 3", "Test Dom Colour 3")
        );

        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(mockMovies);
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(3)));
    }

    @Test
    public void MovieController_GetAllMovies_CallsMovieServiceGetMoviesByCategoryOnce() throws Exception {
        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(List.of());

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk());

        Mockito.verify(movieService, Mockito.times(1)).getMoviesByCategory("popular");
    }

    @Test
    public void MovieController_GetAlMovies_ReturnsCorrectDataToView() throws Exception {
    //Arrange
        List<Movie> mockMovies = List.of(
                new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Dominant Colour")
        );

        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(mockMovies);
    //Act
        MvcResult result = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        List<Movie> movies = (List<Movie>) result.getModelAndView().getModel().get("movies");

    //Assert
        assertThat(movies, Matchers.hasSize(1));

        Movie movie = movies.get(0);
        assertThat(movie.getId(), Matchers.is(1L));
        assertThat(movie.getTitle(), Matchers.is("Test Movie"));
        assertThat(movie.getPosterUrl(), Matchers.is("https://image.tmdb.org/t/p/w780Test Image URL"));
        assertThat(movie.getReleaseDate(), Matchers.is(LocalDate.parse("2025-01-01")));
    }

    @Test
    public void MovieController_GetAllMovies_RenderMoviesWhenMissingPosterUrl() throws Exception {
        Movie movieWithNoPoster = new Movie(1L, "No Poster Movie", null, "2025-01-01", "Dominant Colour");

        Mockito.when(movieService.getMoviesByCategory("popular")).thenReturn(List.of(movieWithNoPoster));

        MvcResult result = mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andReturn();

        List<Movie> movies = (List<Movie>) result.getModelAndView().getModel().get("movies");

        assertThat(movies, Matchers.hasSize(1));

        Movie movie = movies.get(0);
        assertThat(movie.getId(), Matchers.is(1L));
        assertThat(movie.getTitle(), Matchers.is("No Poster Movie"));
        assertThat(movie.getPosterUrl(), Matchers.nullValue());
        assertThat(movie.getReleaseDate(), Matchers.is(LocalDate.parse("2025-01-01")));
    }

    @Test
    public void MovieController_GetAllMovies_UsesDatabaseMoviesWhenNotEmpty() throws Exception {
        List<Movie> dbMovies = List.of(
                new Movie(1L, "DB Movie 1", "URL 1", "2025-01-01", "Colour 1"),
                new Movie(2L, "DB Movie 2", "URL 2", "2025-01-02", "Colour 2")
        );

        Mockito.when(movieService.getAllMoviesFromDatabase()).thenReturn(dbMovies);

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(2)))
                .andExpect(view().name("movies"));

        // Ensure popular fallback is NOT called
        Mockito.verify(movieService, Mockito.never()).getMoviesByCategory("popular");
    }

//    *** searchMovies() ***
    @Test
    public void MovieController_SearchMovies_ReturnsEmptyListWhenNoResults() throws Exception {
        String query = "non-existent";
        Mockito.when(movieService.searchMovies(query)).thenReturn(List.of());

        mockMvc.perform(get("/movies/search").param("q", query))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.empty()))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_SearchMovies_ReturnsSingleMovie() throws Exception {
        String query = "Test Movie";
        List<Movie> result = List.of(new Movie(1L, "Test Movie", "URL", "2025-01-01", "Colour"));
        Mockito.when(movieService.searchMovies(query)).thenReturn(result);

        mockMvc.perform(get("/movies/search").param("q", query))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(1)))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_SearchMovies_ReturnsMultipleMovies() throws Exception {
        String query = "Movie";
        List<Movie> result = List.of(
                new Movie(1L, "Movie 1", "URL 1", "2025-01-01", "Colour 1"),
                new Movie(2L, "Movie 2", "URL 2", "2025-01-02", "Colour 2")
        );
        Mockito.when(movieService.searchMovies(query)).thenReturn(result);

        mockMvc.perform(get("/movies/search").param("q", query))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(2)))
                .andExpect(view().name("movies"));
    }

//    *** getMoviesByCategory() ***
    @Test
    public void MovieController_GetMoviesByCategory_ReturnsEmptyListWhenNoMovies() throws Exception {
        String category = "popular";
        Mockito.when(movieService.getMoviesByCategory(category)).thenReturn(List.of());

        mockMvc.perform(get("/movies/{category}", category))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.empty()))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_GetMoviesByCategory_ReturnsSingleMovie() throws Exception {
        String category = "top_rated";
        List<Movie> movies = List.of(new Movie(1L, "Top Movie", "URL", "2025-01-01", "Colour"));
        Mockito.when(movieService.getMoviesByCategory(category)).thenReturn(movies);

        mockMvc.perform(get("/movies/{category}", category))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(1)))
                .andExpect(view().name("movies"));
    }

    @Test
    public void MovieController_GetMoviesByCategory_ReturnsMultipleMovies() throws Exception {
        String category = "upcoming";
        List<Movie> movies = List.of(
                new Movie(1L, "Upcoming 1", "URL1", "2025-01-01", "Colour1"),
                new Movie(2L, "Upcoming 2", "URL2", "2025-01-02", "Colour2")
        );
        Mockito.when(movieService.getMoviesByCategory(category)).thenReturn(movies);

        mockMvc.perform(get("/movies/{category}", category))
                .andExpect(status().isOk())
                .andExpect(model().attribute("movies", Matchers.hasSize(2)))
                .andExpect(view().name("movies"));
    }

    // *** getMovieDetails() ***
    @Test
    public void MovieController_GetMovieDetails_ReturnsViewAndStatus200() throws Exception {
        // Arrange
        ModelAndView mockMovie = new ModelAndView("colour-palette");
        mockMovie.addObject("movieDetail", new Movie(1L, "Test Movie", "Test Image URL", "2025-01-01", "Dominant Colour"));
        Mockito.when(movieDetailsService.buildMovieDetail(1L)).thenReturn(mockMovie);

        // Act and Assert
        mockMvc.perform(get("/movies/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("colour-palette"));
    }

    @Test
    public void MovieController_GetMovieDetails_CallsServiceWithCorrectId() throws Exception {
        ModelAndView mockMovie = new ModelAndView("colour-palette");
        mockMovie.addObject("movieDetail", new Movie(2L, "Test Movie", "Test Image URL", "2025-01-01", "Dominant Colour"));
        Mockito.when(movieDetailsService.buildMovieDetail(2L)).thenReturn(mockMovie);

        mockMvc.perform(get("/movies/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("colour-palette"));

        Mockito.verify(movieDetailsService).buildMovieDetail(2L);
    }

    @Test
    public void MovieController_GetMovieDetails_CallsServiceOnce() throws Exception {
        // Arrange
        Long movieId = 1L;
        ModelAndView mockMovie = new ModelAndView("colour-palette");
        Mockito.when(movieDetailsService.buildMovieDetail(movieId)).thenReturn(mockMovie);

        // Act
        ModelAndView actual = movieController.getMovieDetails(movieId);

        // Assert
        Mockito.verify(movieDetailsService, Mockito.times(1)).buildMovieDetail(movieId);
        assertEquals(mockMovie, actual);
    }

 @Test
 public void MovieController_GetMovieDetails_ReturnsCorrectMovieDetails() throws Exception {
     //Arrange
     Long movieId = 1L;
     ModelAndView mockView = new ModelAndView("colour-palette");
     mockView.addObject("movieDetail", new Movie(movieId, "Test Movie", "url", "2025-01-01", "Dominant Colour"));
     Mockito.when(movieDetailsService.buildMovieDetail(movieId)).thenReturn(mockView);

     //Act
     mockMvc.perform(get("/movies/{id}", movieId))
     //Assert
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

}