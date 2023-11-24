package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenresDbStorageImpl {
    //получение жанров по айди и всех
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenresDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM Genres",
                new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre objectSearchGenre(int genreId) {
        return jdbcTemplate.query("SELECT * FROM Genres WHERE id=?", new Object[]{genreId},
                        new BeanPropertyRowMapper<>(Genre.class))
                .stream().findAny().orElse(null);
    }

    public List<Genre> getGenresByFilmId(int filmId) {
        return jdbcTemplate.query("SELECT * FROM Genres WHERE id IN " +
                                "(SELECT id_Genre FROM Genres_Film WHERE id_Film=?)", new Object[]{filmId},
                        new BeanPropertyRowMapper<>(Genre.class));

    }

}
