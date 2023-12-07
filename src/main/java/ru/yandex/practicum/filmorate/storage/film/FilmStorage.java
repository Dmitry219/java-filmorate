package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int filmId);

    Film objectSearchFilm(int filmId);
}
