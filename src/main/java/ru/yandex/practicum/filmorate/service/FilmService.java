package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
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

    public List<Film> getSortedFilms(int directorId, String sortBy) {
        return filmStorage.getSortedFilms(directorId, sortBy);
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

    public HashSet<Film> searchFilms(String query, String by) {
        query = query.toLowerCase();
        List<Film> allFilms = filmStorage.getFilms();
        HashSet<Film> result = new HashSet<>();
        if (by.equals("director")) {
            findByDirector(allFilms, result, query);
        } else if (by.equals("title")) {
            findByTitle(allFilms, result, query);
        } else if (by.equals("director,title")) {
            findByDirector(allFilms, result, query);
            findByTitle(allFilms, result, query);
        } else {
            throw new IllegalArgumentException("Некорректное значение параметра by");
        }
        return result;
    }

    private void findByDirector(List<Film> allFilms, HashSet<Film> result, String query) {
        for (Film film : allFilms) {
            for (Director director : film.getDirectors()) {
                if (director.getName().toLowerCase().contains(query)) {
                    result.add(film);
                    break;
                }
            }
        }
    }

    private void findByTitle(List<Film> allFilms, HashSet<Film> result, String query) {
        for (Film film : allFilms) {
            if (film.getName().toLowerCase().contains(query)) {
                result.add(film);
                break;
            }
        }
    }
}
