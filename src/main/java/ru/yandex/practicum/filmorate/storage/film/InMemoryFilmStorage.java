package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.impl.GenresDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorageImpl mpaDbStorage;
    private final GenresDbStorageImpl genresDbStorage;
    private final Validator validator = new Validator();
    private final Map<Integer, Film> saveFilms = new HashMap<>();
    private int generateId = 0;

    @Override
    public Film createFilm(Film film) {
        validator.validate(film);
        film.setId(generateId());
        saveFilms.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validator.validate(saveFilms.get(film.getId()));
        saveFilms.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        saveFilms.remove(filmId);
    }

    @Override
    public Film objectSearchFilm(int filmId) {
        return saveFilms.get(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(saveFilms.values());
    }

    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        return null;
    }

    @Override
    public void addLike(int filmId, int userId) {
    }

    @Override
    public void deleteLike(int filmId, int userId) {
    }

    @Override
    public List<Film> getSortedFilms(int directorId, String sortBy) {
        return null;
    }

    @Override
    public List<Film> searchFilms(String query, String by) {
        return null;
    }

    @Override
    public Integer getMostCommonFilmsUserId(int userId) {
        return null;
    }

    @Override
    public List<Film> getRecommendFilms(Integer id, Integer mostCommonFilmsUserId) {
        return null;
    }

    private int generateId() { //Генерация id
        return ++generateId;
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        return null;
    }
}
