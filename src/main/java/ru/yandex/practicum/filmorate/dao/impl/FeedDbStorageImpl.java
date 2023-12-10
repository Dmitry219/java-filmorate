package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class FeedDbStorageImpl {
    private final JdbcTemplate jdbcTemplate;

    public FeedDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //работа с лайками add delete
    public void addLikeByUserEvent(LocalDateTime currentDateTime, int idUser,
                                   EventType eventType, Operation operation, int idFilm) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser, eventType.toString(), operation.toString(), idFilm);
    }

    public void deleteLikeByUserEvent(LocalDateTime currentDateTime, int idUser,
                                   EventType eventType, Operation operation, int idFilm) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser, eventType.toString(), operation.toString(), idFilm);
    }
    //---------------

    //работа с друзьями add delete
    public void addFriendByUserEvent(LocalDateTime currentDateTime, int idUser,
                                   EventType eventType, Operation operation, int idFriend) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser,
                eventType.toString(), operation.toString(), idFriend);
    }

    public void deleteFriendByUserEvent(LocalDateTime currentDateTime, int idUser,
                                     EventType eventType, Operation operation, int idFriend) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                        " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser,
                eventType.toString(), operation.toString(), idFriend);
    }
    //---------------

    //работа с отзывами add delete update
    public void addReviewByUserEvent(LocalDateTime currentDateTime, int idUser,
                                     EventType eventType, Operation operation, int idFriend) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                        " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser,
                eventType.toString(), operation.toString(), idFriend);
    }

    public void updateReviewByUserEvent(LocalDateTime currentDateTime, int idUser,
                                     EventType eventType, Operation operation, int idFriend) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                        " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser,
                eventType.toString(), operation.toString(), idFriend);
    }

    public void deleteReviewByUserEvent(LocalDateTime currentDateTime, int idUser,
                                        EventType eventType, Operation operation, int idFriend) {
        jdbcTemplate.update("INSERT INTO feed (timestamp, userId, eventType, operation, entityId)" +
                        " VALUES (?, ?, ?, ?, ?)", currentDateTime, idUser,
                eventType.toString(), operation.toString(), idFriend);
    }
    //---------------

    public List<Feed> getEvent(int id) {
        return jdbcTemplate.query("SELECT * FROM FEED f WHERE USERID=?;",new FeedMapper(),id);
    }
}
