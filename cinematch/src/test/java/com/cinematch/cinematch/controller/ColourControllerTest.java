package com.cinematch.cinematch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc

public class ColourControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testImageColourRedirect() throws Exception {
        mockMvc.perform(get("/image-colour")
                        .param("url", "https://image.tmdb.org/t/p/w200/ynT06XivgBDkg7AtbDbX1dJeBGY.jpg")
                        .param("movieId", "1263256"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/colours/1263256/1e6dc9"));
    }
}


