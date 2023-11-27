package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dao.impl.*;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {MpaDbStorageImpl.class, GenresDbStorageImpl.class, LikesDbStorageImpl.class, FilmDbStorageImpl.class,
        UserDbStorageImpl.class, FriendshipDbStorageImpl.class})
public class FilmDbStorageTest {
    private final FilmDbStorageImpl filmDbStorage;
    private final UserDbStorageImpl userDbStorage;
    private Validator validator = new Validator();
    Film film;
    Film film1;
    User user;
    Mpa mpa;
    Mpa mpa1;
    Mpa mpa2;
    Mpa mpa3;
    Mpa mpa4;
    Genre genre;
    Genre genre1;
    Genre genre2;
    Genre genre3;
    Genre genre4;
    Genre genre5;
    List<Genre> genres = new ArrayList<>();

    @BeforeEach
    void create() {
        mpa = new Mpa();
        mpa.setId(1);
        mpa.setName("G");

        mpa1 = new Mpa();
        mpa1.setId(2);
        mpa1.setName("PG");

        mpa2 = new Mpa();
        mpa2.setId(3);
        mpa2.setName("PG-13");

        mpa3 = new Mpa();
        mpa3.setId(4);
        mpa3.setName("R");

        mpa4 = new Mpa();
        mpa4.setId(5);
        mpa4.setName("NC-17");

        genre = new Genre();
        genre.setId(1);
        genre.setName("Комедия");

        genre1 = new Genre();
        genre1.setId(1);
        genre1.setName("Драма");

        genre2 = new Genre();
        genre2.setId(1);
        genre2.setName("Мультфильм");

        genre3 = new Genre();
        genre3.setId(1);
        genre3.setName("Триллер");

        genre4 = new Genre();
        genre4.setId(1);
        genre4.setName("Документальный");

        genre5 = new Genre();
        genre5.setId(1);
        genre5.setName("Боевик");

        film = new Film();
        film.setName("dima");
        film.setDuration(100);
        film.setDescription("Test data");
        film.setReleaseDate(LocalDate.of(1999, 03,25));
        film.setMpa(mpa);
        genres.add(genre);
        film.setGenres(genres);

        filmDbStorage.createFilm(film);

//        film1 = new Film();
//        film1.setName("Update_dima");
//        film1.setDuration(50);
//        film1.setDescription("Update Test data");
//        film1.setReleaseDate(LocalDate.of(2000, 03,25));
//        film.setMpa(mpa1);
//        genres.add(genre1);
//        film.setGenres(genres);
//
//        filmDbStorage.createFilm(film1);

//        user = new User();
//        user.setEmail("mail@mail.ru");
//        user.setName("dolore");
//        user.setId(1);
//        user.setBirthday(LocalDate.of(1988,07,11));
//        userDbStorage.createUser(user);
    }

    @Test
    void shouldCreateFilm() {
        Film savedFilm = filmDbStorage.createFilm(film);
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void updateFilmTrue() {

        film.setName("Update_dima");
        film.setDuration(50);
        film.setDescription("Update Test data");
        film.setReleaseDate(LocalDate.of(1999, 03,25));
        film.setMpa(mpa1);
        genres.add(genre1);
        film.setGenres(genres);

        Film savedFilm = filmDbStorage.updateFilm(film);
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }
}
