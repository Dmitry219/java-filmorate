package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public Film createFilm(Film film);

    public Film updateFilm(Film film);

    public void deleteFilm(int filmId);

    public Film objectSearchFilm(int filmId);

    public List<Film> getFilms();

    public List<Film> getPopularFilms(int size);

    public void addLike(int filmId, int userId);

    public void deleteLike(int filmId, int userId);
    Film getFilmById (int filmId);
}
