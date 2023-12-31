package ru.yandex.practicum.filmorate.dao.impl;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

public interface FilmDbStorage extends FilmStorage {
    List<Film> getSortedFilms(int directorId, String sortBy);

    List<Film> searchFilms(String query, String by);

    Integer getMostCommonFilmsUserId(int userId);

    List<Film> getRecommendFilms(Integer id, Integer mostCommonFilmsUserId);

    List<Film> getFilms();

    List<Film> getPopularFilms(int count, Integer genreId, Integer year);

    List<Film> getCommonFilms(int userId, int friendId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);
}
