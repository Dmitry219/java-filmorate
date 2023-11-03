package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
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

    private int generateId() { //Генерация id
        return ++generateId;
    }
}