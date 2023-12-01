package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FriendshipDbStorageImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipDbStorageImpl friendshipDbStorage;

    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendshipDbStorageImpl friendshipDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }

    public void addFriends(int userId, int friendId) {
        friendshipDbStorage.addFriends(userId, friendId);
        log.info("Довление в друзья к пользователю {} добовляемый пользователь {}", userId, friendId);
    }

    public void deleteFriends(int userId, int friendId) {
        friendshipDbStorage.deleteFriendByUserId(userId, friendId);
        log.info("Довление в друзья к пользователю {} добовляемый пользователь {}", userId, friendId);
    }

    //получить друзей конкретонго пользовятеля
    public List<User> getOfFriendsOfASpecificUser(int userId) {
        log.info("Получение в UserService id {} пользователя", userId);
        return friendshipDbStorage.getFriendByUserId(userId);
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        log.info("Получение списка общих друзей пользовтеля {} с пользователем {}", userId, friendId);
        List<User> f = new ArrayList<>();
                f = friendshipDbStorage.getListOfFriendsSharedWithAnotherUser(userId, friendId);
        log.info("СПИСОК f === {}", f);
        return f;
    }

    //------------------методы UserStorage-------------------
    public User createUser(User user) {
        log.info("Создание пользователя {}", user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        log.info("Обновление пользователя {}", user);
        return userStorage.updateUser(user);
    }

    public void deleteUser(int userId) {
        log.info("Удаление пользователя {}", userId);
        userStorage.deleteUser(userId);
    }

    public User objectSearchUser(int userId) {
        log.info("Поиск пользователя {}", userId);
        return userStorage.objectSearchUser(userId);
    }

    public List<User> getUsers() {
        log.info("Получение списка всех ползовтелей");
        return userStorage.getUsers();
    }
}
