package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Добовление лайка фильму {} от пользователя {}", filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.deleteLike(filmId, userId);
        log.info("Удаление лайка у фильма {} от пользователя {}", filmId, userId);
    }

    public List<Film> getPopularFilms(int size) {
        log.info("Возвращение списка размером {} популярных фильмов", size);
        return filmStorage.getPopularFilms(size);
    }

    public Film createFilm(Film film) {
        log.info("Создание фильма {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Обновление фильма {}", film);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int filmId) {
        log.info("Удаление фильма {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    public Film objectSearchFilm(int filmId) {
        log.info("Поиск фильма {}", filmId);
        return filmStorage.objectSearchFilm(filmId);
    }

    public List<Film> getFilms() {
        log.info("Возварщение всех фильмов");
        return filmStorage.getFilms();
    }
}
