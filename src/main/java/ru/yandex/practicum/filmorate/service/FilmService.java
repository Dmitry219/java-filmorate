package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final Validator validator = new Validator();

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getSortedFilms(int directorId, String sortBy) {
       return filmStorage.getSortedFilms(directorId, sortBy);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Добавление лайка фильму {} от пользователя {}", filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        checkId(filmId);
        checkId(userId);
        filmStorage.deleteLike(filmId, userId);
        log.info("Удаление лайка у фильма {} от пользователя {}", filmId, userId);
    }

    public List<Film> getPopularFilmsByGenreAndYear(int count, Integer genreId, Integer year) {
        log.info("Возвращение списка популярных фильмов по жанру и годам {} {} {}", count, genreId, year);
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        log.info("Получение общих фильмов у пользвоателей с id {} и {}", userId, friendId);
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        return films;
    }

    public Film createFilm(Film film) {
        validator.validate(film);
        log.info("Создание фильма {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        checkId(film.getId());
        validator.validate(film);
        log.info("Обновление фильма {}", film);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(int filmId) {
        log.info("Удаление фильма {}", filmId);
        filmStorage.deleteFilm(filmId);
    }

    public Film objectSearchFilm(int filmId) {
        checkId(filmId);
        log.info("Поиск фильма {}", filmId);
        return filmStorage.objectSearchFilm(filmId);
    }

    public List<Film> getFilms() {
        log.info("Возвращение всех фильмов");
        return filmStorage.getFilms();
    }

    public void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("FilmId не может быть меньше нуля или равен нулю!");
        } else if (filmStorage.objectSearchFilm(id).equals(null)) {
            throw new RuntimeException("Фильм с таким Id не существует!");
        }
    }
}

