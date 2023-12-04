package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

import java.time.LocalDateTime;

@Component
@Slf4j
public class FeedDbStorageImpl {
    private final JdbcTemplate jdbcTemplate;

    public FeedDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //работа с лайками add delete update
    public void addLikeByUserEvent(LocalDateTime currentDateTime, int idUser,
                                   EventType eventType, Operation operation, int idFilm){
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser, eventType, operation, idFilm);
    }
    //---------------
}
