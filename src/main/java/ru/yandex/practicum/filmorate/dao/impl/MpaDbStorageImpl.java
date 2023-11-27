package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
public class MpaDbStorageImpl {
    //получение жанров по айди и всех
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getMpa() {
        return jdbcTemplate.query("SELECT * FROM MPA",
                        new BeanPropertyRowMapper<>(Mpa.class));
    }

    public Mpa objectSearchMpa(int mpaId) {
        return jdbcTemplate.query("SELECT * FROM MPA WHERE id=?", new Object[]{mpaId},
                        new BeanPropertyRowMapper<>(Mpa.class))
                .stream().findAny().orElse(null);
    }
}
