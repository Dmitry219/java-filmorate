package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@Qualifier("FilmDbStorage")
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorageImpl mpaDbStorage;
    private final GenresDbStorageImpl genresDbStorage;
    private final DirectorDbStorageImpl directorDbStorage;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, MpaDbStorageImpl mpaDbStorage,
                             GenresDbStorageImpl genresDbStorage, DirectorDbStorageImpl directorDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genresDbStorage = genresDbStorage;
        this.directorDbStorage = directorDbStorage;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("id");

        Number key = simpleJdbcInsert.executeAndReturnKey(film.toMap());

        for (Genre g : film.getGenres()) {
            log.info("ПРОВЕРКА ЖАНРА {} ", g);
            jdbcTemplate.update("INSERT INTO Genres_Film (id_Film, id_Genre) VALUES (?, ?)",
                    key.intValue(), g.getId());
        }
        for (Director director : film.getDirectors()) {
            log.info("ПРОВЕРКА режисёра {} ", director);
            jdbcTemplate.update("INSERT INTO Directors_Film (id_Film, id_Director) VALUES (?, ?)",
                    key.intValue(), director.getId());
        }
        film.setId(key.intValue());
        film.setMpa(mpaDbStorage.objectSearchMpa(film.getMpa().getId()));
        film.setGenres(genresDbStorage.getGenresByFilmId(key.intValue()));
        film.setDirectors(directorDbStorage.getDirectorByFilmId(key.intValue()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE Films SET name=?, description=?, release_Date=?, duration=?," +
                        "id_MPA=? WHERE id=?", film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update("DELETE Genres_Film WHERE id_Film=?", film.getId());
        for (Genre g : film.getGenres()) {
            log.info("ПРОВЕРКА ЖАНРА {} ", g);
            jdbcTemplate.update("INSERT INTO Genres_Film (id_Film, id_Genre) VALUES (?, ?)",
                    film.getId(), g.getId());
        }
        jdbcTemplate.update("DELETE Directors_Film WHERE id_Film=?", film.getId());

        for (Director director : film.getDirectors()) {
            jdbcTemplate.update("INSERT INTO Directors_Film (id_Film, id_Director) VALUES (?, ?)",
                    film.getId(), director.getId());
        }

        film.setMpa(mpaDbStorage.objectSearchMpa(film.getMpa().getId()));
        film.setGenres(genresDbStorage.getGenresByFilmId(film.getId()));
        film.setDirectors(directorDbStorage.getDirectorByFilmId(film.getId()));
        return film;
    }

    @Override
    public List<Film> getSortedFilms(int directorId, String sortBy) {
        checkIdDirectir(directorId);
        log.info("ПРОВЕРКА directorId={} и  sortBy={}", directorId, sortBy);

        if (sortBy.equals("year")) {
            return jdbcTemplate.query("SELECT f.* FROM FILMS f WHERE id IN" +
                            " (SELECT df.id_film FROM DIRECTORS_FILM df WHERE id_director=?)" +
                            " GROUP BY f.RELEASE_DATE" +
                            " ORDER  BY f.RELEASE_DATE;",
                    new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), directorId);
        } else if (sortBy.equals("likes")) {
            return jdbcTemplate.query("SELECT * " +
                            "FROM FILMS f " +
                            "LEFT JOIN DIRECTORS_FILM df ON f.ID  = df.ID_FILM " +
                            "LEFT JOIN LIKES l ON l.ID_FILM = df.ID_FILM " +
                            "WHERE df.ID_DIRECTOR = ?" +
                            "GROUP BY f.ID " +
                            "ORDER BY COUNT(l.ID_USER);",
                    new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), directorId);
        }
        return null;
    }

    @Override
    public void addLike(int filmId, int userId) {
        log.info("ПРОВЕРКА filmId={} и  userId={}", filmId, userId);
        jdbcTemplate.update("INSERT INTO Likes (id_Film, id_User) VALUES (?, ?)",
                filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM Likes WHERE id_Film=? AND id_User=?",
                filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count, Integer genreId, Integer year) {
        if (Objects.equals(genreId, year)) {
            final String sqlQuery = "SELECT f.* " +
                    "FROM Films AS f " +
                    "LEFT JOIN Likes AS l ON f.id = l.id_Film " +
                    "GROUP BY f.id " +
                    "ORDER BY COUNT(l.id_User) DESC " +
                    "LIMIT ?;";
            return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), count);
        } else {
            if (genreId == null) {
                final String sqlQuery = "SELECT f.* " +
                        "FROM Films AS f " +
                        "LEFT JOIN Likes AS l ON f.id = l.id_Film " +
                        "WHERE EXTRACT(YEAR FROM CAST(f.release_Date AS date)) = ? " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.id_User) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), year, count);
            } else if (year == null) {
                final String sqlQuery = "SELECT f.* " +
                        "FROM Films AS f " +
                        "LEFT JOIN Genres_Film AS gf ON f.id = gf.id_Film " +
                        "LEFT JOIN Likes AS l ON f.id = l.id_Film " +
                        "WHERE gf.id_Genre = ? " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.id_User) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), genreId, count);
            } else {
                final String sqlQuery = "SELECT f.* " +
                        "FROM Films AS f " +
                        "LEFT JOIN Genres_Film AS gf ON f.id = gf.id_Film " +
                        "LEFT JOIN Likes AS l ON f.id = l.id_Film " +
                        "WHERE EXTRACT(YEAR FROM CAST(f.release_Date AS date)) = ? " +
                        "AND gf.id_Genre = ? " +
                        "GROUP BY f.id " +
                        "ORDER BY COUNT(l.id_User) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), year, genreId, count);
            }
        }
    }

    public List<Film> getCommonFilms(int userId, int friendId) {
        log.info("Получение общих фильмов от пользователей {} {}", userId, friendId);
        String sqlQuery = "SELECT f.* " +
                "FROM Films AS f, Likes AS l1, Likes AS l2 " +
                "WHERE f.id = l1.id_Film " +
                "AND f.id = l2.id_Film " +
                "AND l1.id_User = ? " +
                "AND l2.id_User = ? " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(*) DESC;";

        return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage), userId, friendId);
    }

    @Override
    public void deleteFilm(int filmId) {
        jdbcTemplate.update("DELETE FROM Films WHERE id=?", filmId);
    }

    @Override
    public Film objectSearchFilm(int filmId) {
        return jdbcTemplate.query("SELECT * FROM Films WHERE id=?", new Object[]{filmId},
                        new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage))
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM Films", new FilmMapper(mpaDbStorage, genresDbStorage,
                directorDbStorage));
    }

    private void checkIdDirectir(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (directorDbStorage.objectSearchDirector(id).equals(null)) {
            throw new RuntimeException("Фильм с таким Id не существует!");
        }
    }

    public List<Film> searchFilms(String filmForSearch, String by) {
        if (by.equalsIgnoreCase("director")) {
            return jdbcTemplate.query(
                        "SELECT f.*, COUNT(l.id_Film) AS like_count " +
                            "FROM Films f " +
                            "JOIN Directors_Film df ON f.id = df.id_film " +
                            "JOIN Directors d ON df.id_director = d.id " +
                            "LEFT JOIN Likes l ON f.id = l.id_Film " +
                            "WHERE LOWER(d.name) LIKE '%' || ? || '%' " +
                            "ORDER BY like_count DESC", new Object[]{filmForSearch.toLowerCase()}, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage));
        } else if (by.equalsIgnoreCase("title")) {
            return jdbcTemplate.query(
                        "SELECT f.*, COUNT(l.id_film) AS like_count " +
                            "FROM Films f " +
                            "LEFT JOIN Likes l ON f.id = l.id_film " +
                            "WHERE LOWER(f.name) LIKE '%' || ? || '%' " +
                            "ORDER BY like_count DESC", new Object[]{filmForSearch.toLowerCase()}, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage));
        } else if (by.equalsIgnoreCase("director,title") || by.equalsIgnoreCase("title,director")) {
            return jdbcTemplate.query(
                    "SELECT f.*, COUNT(l.id_Film) AS like_count " +
                            "FROM Films f " +
                            "LEFT JOIN Directors_Film df ON f.id = df.id_film " +
                            "LEFT JOIN Directors d ON df.id_director = d.id " +
                            "LEFT JOIN Likes l ON f.id = l.id_Film " +
                            "WHERE LOWER(f.name) LIKE '%' || ? || '%' OR LOWER(d.name) LIKE '%' || ? || '%' " +
                            "GROUP BY f.id " +
                            "ORDER BY like_count DESC", new Object[]{filmForSearch.toLowerCase(), filmForSearch.toLowerCase()}, new FilmMapper(mpaDbStorage, genresDbStorage, directorDbStorage));
        } else {
            throw new IllegalArgumentException("Некорректное значение параметра by");
        }
    }
}
