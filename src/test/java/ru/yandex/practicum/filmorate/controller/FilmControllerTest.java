package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.annotation.DirtiesContext;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    public static final String PATH = "/films";
    @Autowired
     private MockMvc mockMvc;

    @Test
    void createFilm() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.post(PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(getContentFromFilm("controller/request/film.json")))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(
                getContentFromFilm("controller/response/film.json")
        ));
    }

    @Test
    void createFilmNegative() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-release-date-empty.json")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateFilm() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film.json")))
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-update-empty.json")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/film-update-empty.json")
                ));
    }

    @Test
    void getFilms() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/filmGet.json")))
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/filmGet.json")
                ));
    }


    private String getContentFromFilm(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
            StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}