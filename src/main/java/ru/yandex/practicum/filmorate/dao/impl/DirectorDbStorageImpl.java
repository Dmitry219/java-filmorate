package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;

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
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Directors")
                .usingGeneratedKeyColumns("id");

        Number key = simpleJdbcInsert.executeAndReturnKey(director.toMap());
        director.setId(key.intValue());
        return director;
    }

    public Director updateDirector(Director director) {
        log.info("ПОЛУЧИЛИ режисёра для обновления {} ", director);
        jdbcTemplate.update("UPDATE Directors SET name=? WHERE id=?", director.getName(), director.getId());
        log.info("ВЕРНУЛСЯ режисёр обновленый {} ", director);
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

    public List<Director> getDirectorByFilmId(int filmId) {
        return jdbcTemplate.query("SELECT * FROM Directors WHERE id IN " +
                        "(SELECT id_Director FROM Directors_Film WHERE id_Film=?)", new Object[]{filmId},
                new BeanPropertyRowMapper<>(Director.class));

    }

    public void deleteDirector(int directorId) {
        //jdbcTemplate.update("DELETE FROM Directors_Film WHERE id_Director=?", directorId);
        jdbcTemplate.update("DELETE FROM Directors WHERE id=?", directorId);
    }
}
