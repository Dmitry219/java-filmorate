package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@Slf4j
public class DirectorDbStorageImpl {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Director createDirector(Director director) {
        jdbcTemplate.update("INSERT INTO Directors (name) VALUES (?)", director.getName());
        return director;
    }

    public List<Director> getDirectors() {
        return jdbcTemplate.query("SELECT * FROM Directors",
                new BeanPropertyRowMapper<>(Director.class));
    }

    public Director objectSearchDirector(int genreId) {
        return jdbcTemplate.query("SELECT * FROM Directors WHERE id=?", new Object[]{genreId},
                        new BeanPropertyRowMapper<>(Director.class))
                .stream().findAny().orElse(null);
    }
}
