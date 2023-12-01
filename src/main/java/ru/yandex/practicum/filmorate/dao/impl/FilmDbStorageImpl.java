package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Component
@Slf4j
@Qualifier("FilmDbStorage")
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorageImpl mpaDbStorage;
    private final GenresDbStorageImpl genresDbStorage;

    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate, MpaDbStorageImpl mpaDbStorage, GenresDbStorageImpl genresDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genresDbStorage = genresDbStorage;
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
        film.setId(key.intValue());
        film.setMpa(mpaDbStorage.objectSearchMpa(film.getMpa().getId()));
        film.setGenres(genresDbStorage.getGenresByFilmId(key.intValue()));
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
        film.setMpa(mpaDbStorage.objectSearchMpa(film.getMpa().getId()));
        film.setGenres(genresDbStorage.getGenresByFilmId(film.getId()));
        return film;
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

        return jdbcTemplate.query(sqlQuery, new FilmMapper(mpaDbStorage, genresDbStorage), userId, friendId);
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        return jdbcTemplate.query("SELECT f.* FROM FILMS f LEFT JOIN LIKES l ON f.ID = l.ID_FILM " +
                "GROUP BY f.ID " +
                "ORDER BY COUNT(l.ID_USER) DESC " +
                "LIMIT ?", new FilmMapper(mpaDbStorage, genresDbStorage), size);
    }

    @Override
    public void deleteFilm(int filmId) {
        jdbcTemplate.update("DELETE FROM Films WHERE id=?", filmId);
    }

    @Override
    public Film objectSearchFilm(int filmId) {
        return jdbcTemplate.query("SELECT * FROM Films WHERE id=?", new Object[]{filmId},
                        new FilmMapper(mpaDbStorage, genresDbStorage))
                .stream().findAny().orElse(null);
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM Films", new FilmMapper(mpaDbStorage, genresDbStorage));
    }
}
