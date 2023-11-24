/*
package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.service.FilmService;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {
    public static final String PATH = "/films";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;
    @Autowired
    private FilmService filmService;
    private Validator validator = new Validator();
    Film film;
    Film film1;
    User user;

    @BeforeEach
    void create() {
        film = new Film();
        film.setName("dima");
        film.setDuration(100);
        film.setDescription("Test data");
        film.setReleaseDate(LocalDate.of(1999, 03,25));
        filmService.createFilm(film);

        film1 = new Film();
        film1.setName("aaaa");
        film1.setDuration(151);
        film1.setDescription("Test data");
        film1.setReleaseDate(LocalDate.of(2000, 03,25));
        filmService.createFilm(film1);

        user = new User();
        user.setEmail("mail@mail.ru");
        user.setName("dolore");
        user.setId(1);
        user.setBirthday(LocalDate.of(1988,07,11));
    }

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
                .andExpect(status().is5xxServerError());
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
                .andExpect(status().is5xxServerError());

    }

    @Test
    void validateFilmNegativeNoName() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-NoName.json")))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void validateFilmNegativeMaxSize() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-Max-Size.json")))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void validateFilmNegativeDateLimit() throws Exception {
        film.setReleaseDate(LocalDate.of(1890, 03,25));

        Exception exception = Assertions.assertThrows(ValidationException.class, () -> validator.validate(film));

        String expectedMessage = "Старая дата релиза фильма!";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-release-date-empty.json")))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void validateFilmNegativeDurationNumber() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/film-Positiv-number.json")))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void getFilms() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/filmsGet.json")
                ));
    }

    //--------------Тесты нового функционала------------------------------
    @Test
    void addingLikesTrue() {
        filmService.addLike(film.getId(), user.getId());

        Assertions.assertTrue(film.getLikes().contains(user.getId()), "Лайк не добавился!");
    }

    @Test
    void deletionOfLikeFalse() {
        filmService.addLike(film.getId(), user.getId());
        Assertions.assertTrue(film.getLikes().contains(user.getId()), "лайк не добавился");

        filmService.deleteLike(film.getId(), user.getId());
        Assertions.assertFalse(film.getLikes().contains(user.getId()), "лайк не удалился");
    }

    @Test
    void getListFilmsPopularEqualsTrue() {
        filmService.addLike(film.getId(), user.getId());

        Assertions.assertEquals(film, filmService.getPopularFilms(1).get(0), "фильмы не равны!");
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

 */