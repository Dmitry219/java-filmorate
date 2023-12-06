package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm(int filmId);

    Film objectSearchFilm(int filmId);

    List<Film> getFilms();

    List<Film> getPopularFilms(int count, Integer genreId, Integer year);

    List<Film> getCommonFilms(int userId, int friendId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getSortedFilms(int directorId, String sortBy);

    List<Film> searchFilms(String query, String by);

    Integer getMostCommonFilmsUserId(int userId);

    List<Film> getRecommendFilms(Integer id, Integer mostCommonFilmsUserId);
}
