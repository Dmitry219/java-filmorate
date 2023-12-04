package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FeedDbStorageImpl;
import ru.yandex.practicum.filmorate.dao.impl.FriendshipDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipDbStorageImpl friendshipDbStorage;
    private final Validator validator = new Validator();
    private final FeedDbStorageImpl feedDbStorage;


    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage,
                       FriendshipDbStorageImpl friendshipDbStorage, FeedDbStorageImpl feedDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;
        this.feedDbStorage = feedDbStorage;
    }

    public void addFriends(int userId, int friendId) {
        checkId(userId);
        checkId(friendId);
        friendshipDbStorage.addFriends(userId, friendId);
        log.info("Добавление в друзья к пользователю {} добавляемый пользователь {}", userId, friendId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        feedDbStorage.addFriendByUserEvent(currentDateTime,userId, EventType.FRIEND,
                Operation.ADD, friendId);
    }

    public void deleteFriends(int userId, int friendId) {
        checkId(userId);
        checkId(friendId);
        friendshipDbStorage.deleteFriendByUserId(userId, friendId);
        log.info("Добавление в друзья к пользователю {} добавляемый пользователь {}", userId, friendId);
        LocalDateTime currentDateTime = LocalDateTime.now();
        feedDbStorage.deleteFriendByUserEvent(currentDateTime,userId, EventType.FRIEND,
                Operation.REMOVE, friendId);
    }

    //получить друзей конкретонго пользовятеля
    public List<User> getOfFriendsOfASpecificUser(int userId) {
        log.info("Получение в UserService id {} пользователя", userId);
        checkId(userId);
        return friendshipDbStorage.getFriendByUserId(userId);
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        checkId(userId);
        checkId(friendId);
        log.info("Получение списка общих друзей пользователя {} с пользователем {}", userId, friendId);
        List<User> f = new ArrayList<>();
                f = friendshipDbStorage.getListOfFriendsSharedWithAnotherUser(userId, friendId);
        log.info("СПИСОК f === {}", f);
        return f;
    }

    //------------------методы UserStorage-------------------
    public User createUser(User user) {
        validator.validate(user);
        log.info("Создание пользователя {}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        checkId(user.getId());
        validator.validate(user);
        log.info("Обновление пользователя {}", user);
        return userStorage.updateUser(user);
    }

    public void deleteUser(int userId) {
        checkId(userId);
        log.info("Удаление пользователя {}", userId);
        userStorage.deleteUser(userId);
    }

    public User objectSearchUser(int userId) {
        checkId(userId);
        log.info("Поиск пользователя {}", userId);
        return userStorage.objectSearchUser(userId);
    }

    public List<User> getUsers() {
        log.info("Получение списка всех пользовтелей");
        return userStorage.getUsers();
    }

    public List<Feed> getEvent(int id) {
        checkId(id);
        return feedDbStorage.getEvent(id);
    }

    public void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("UserId не может быть меньше нуля или равен нулю!");
        } else if (userStorage.objectSearchUser(id).equals(null)) {
            throw new RuntimeException("Пользователь с таким Id не существует!");
        }
    }
}