package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.dao.impl.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {MpaDbStorageImpl.class, GenresDbStorageImpl.class, LikesDbStorageImpl.class, FilmDbStorageImpl.class,
        UserDbStorageImpl.class, FriendshipDbStorageImpl.class, DirectorDbStorageImpl.class})
public class UserDbStorageTest {
    private Validator validator = new Validator();
    private final UserDbStorageImpl userDbStorage;
    private final FriendshipDbStorageImpl friendshipDbStorage;
    User user;
    User user1;
    User user2;
    private Set<Integer> friends = new HashSet<>();

    @BeforeEach
    void create() {
        friends.add(1);

        user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("test_1");
        user.setName("dolore");
        user.setBirthday(LocalDate.of(1988,07,11));
        user.setFriends(friends);
        userDbStorage.createUser(user);

//        user1 = new User();
//        user1.setEmail("mailNd@mail.ru");
//        user.setLogin("test_2");
//        user1.setName("qqqqqqq");
//        user1.setBirthday(LocalDate.of(2000,11,11));
//        user1.setFriends(friends);
//        userDbStorage.createUser(user1);

//        user2 = new User();
//        user2.setEmail("mailDs@mail.ru");
//        user2.setName("wwwwww");
//        user2.setBirthday(LocalDate.of(1999,05,05));
//        userDbStorage.createUser(user2);
    }

    @Test
    void shouldCreateUser() {
        User savedUser = userDbStorage.createUser(user);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void updateFilmTrue() {

        user.setEmail("update@mail.ru");
        user.setLogin("Update_test_1");
        user.setName("test_@");
        user.setBirthday(LocalDate.of(1988,07,11));
        user.setFriends(friends);

        User savedUser = userDbStorage.updateUser(user);
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }
}
