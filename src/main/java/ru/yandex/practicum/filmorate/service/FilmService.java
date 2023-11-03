package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        objectSearchFilm(filmId).addLikes(userId);
    }

    public void deleteLike(int filmId, int userId) {
        objectSearchFilm(filmId).deleteLikes(userId);
    }

    public List<Film> getPopularFilms(int size) {
        return filmStorage.getFilms().stream()
                        .sorted((film1, film2) -> {
                            return film2.getLikesCount() - film1.getLikesCount();
                        })
                        .limit(size)
                        .collect(Collectors.toList());
    }

    //------------------методы FilmStorage-------------------
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public Film objectSearchFilm(int filmId) {
        return filmStorage.objectSearchFilm(filmId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
