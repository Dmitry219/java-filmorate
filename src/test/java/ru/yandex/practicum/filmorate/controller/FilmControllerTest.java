package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
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
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    public static final String PATH = "/films";
    @Autowired
     private MockMvc mockMvc;
    @Autowired
    private FilmController filmController = new FilmController();
    private Validator validator = new Validator();

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
    void validateFilmNegativeBlank() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-BlankName.json")))
                .andExpect(status().is4xxClientError());

    }

    @Test
    void validateFilmNegativeNoName() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-NoName.json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    void validateFilmNegativeMaxSize() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-Max-Size.json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    void validateFilmNegativeDateLimit() throws Exception {
        Film film = new Film();
        film.setId(1);
        film.setName("dima");
        film.setDuration(100);
        film.setDescription("Test data");
        film.setReleaseDate(LocalDate.of(1890, 03,25));

        Exception exception = Assertions.assertThrows(ValidationException.class, () ->
        {validator.validate(film);});

        String expectedMessage = "Старая дата релиза фильма!";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-release-date-empty.json")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validateFilmNegativeDurationNumber() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-Positiv-number.json")))
                .andExpect(status().isBadRequest());

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