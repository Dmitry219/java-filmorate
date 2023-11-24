package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FriendshipDbStorageImpl {
    // методы получения всех друзей пользователя и по id , доабалвения , удаления
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorageImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriends(int userId, int friendId){
        log.info("Получили id user {} и id friend ", userId, friendId);
        jdbcTemplate.update("INSERT INTO Friendship VALUES (?, ?)" , userId, friendId);
    }

    public List<User> getFriendByUserId(int userId) {
        log.info("ПОЛУЧИЛ в Db id user {}", userId);
        List<User> friend = new ArrayList<>();
//         jdbcTemplate.query("SELECT * FROM Users WHERE id IN " +
//                        "(SELECT id_Friend FROM Friendship WHERE id_User=?)", new Object[]{userId},
//               new UserMapper());
         friend = jdbcTemplate.query("SELECT * FROM Users WHERE id IN " +
                         "(SELECT id_Friend FROM Friendship WHERE id_User=?)", new Object[]{userId},
                 new UserMapper());
        log.info("СПИСОК друзей {}", friend);
         return friend;
    }

    public void deleteFriendByUserId(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM Friendship WHERE id_User=? AND id_Friend=?", userId, friendId);
    }

    public List<User> getListOfFriendsSharedWithAnotherUser (int id, int otherId){
        return jdbcTemplate.query("SELECT * " +
                "FROM Users u WHERE id IN (" +
                "SELECT f.id_Friend FROM Friendship f " +
                "INNER JOIN Friendship f2 ON f.id_Friend = f2.id_Friend" +
                " WHERE f.id_User=? AND f2.id_User=?)",new BeanPropertyRowMapper<>(User.class) , id, otherId);
    }
}
