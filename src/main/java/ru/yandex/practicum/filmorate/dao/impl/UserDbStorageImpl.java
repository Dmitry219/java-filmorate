package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Component
@Qualifier("UserDbStorage")
public class UserDbStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");

        Number key = simpleJdbcInsert.executeAndReturnKey(user.toMap());
        user.setId(key.intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE Users SET email=?, login=?, name=?, birthday=? WHERE id=?", user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        jdbcTemplate.update("DELETE FROM Users WHERE id=?", userId);
    }

    @Override
    public User objectSearchUser(int userId) {
        return jdbcTemplate.query("SELECT * FROM Users WHERE id=?", new Object[]{userId},
                        new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM Users", new BeanPropertyRowMapper<>(User.class));
    }
}
