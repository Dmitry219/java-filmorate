package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.impl.GenresDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.HashMap;
import java.util.Map;

@Repository
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

    private int generateId() { //Генерация id
        return ++generateId;
    }
}
