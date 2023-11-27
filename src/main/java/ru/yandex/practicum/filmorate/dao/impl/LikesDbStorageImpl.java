package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class LikesDbStorageImpl {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO Likes (id_Film, id_User) VALUES (?, ?)",
                filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM Likes WHERE id_Film=? AND id_User=?",
                filmId, userId);
    }

}
