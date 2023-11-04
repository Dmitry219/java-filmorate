package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(int userId, int friendId) {
        objectSearchUser(userId).addFriends(friendId);//у user появляется друг friend
        objectSearchUser(friendId).addFriends(userId);//у friend появляется друг user
        log.info("Довление в друзья к пользователю {} добовляемый пользователь {}", userId, friendId);
    }

    public void deleteFriends(int userId, int friendId) {
        objectSearchUser(userId).deleteFriends(friendId);//у user удалить друг friend
        objectSearchUser(friendId).deleteFriends(userId);//у friend удалить друг user
        log.info("Довление в друзья к пользователю {} добовляемый пользователь {}", userId, friendId);
    }

    //получить друзей конкретонго пользовятеля
    public List<User> getOfFriendsOfASpecificUser(int userId) {
        List<User> friends = new ArrayList<>();
        for (Integer id : objectSearchUser(userId).getFriends()) {
            friends.add(objectSearchUser(id));
            log.info("Пользователь {}", objectSearchUser(id));
        }
        log.info("Получение списка друзей пользователя {}", userId);
        return friends;
    }

    public List<User> getMutualFriends(int userId, int friendId) {
        List<User> users = new ArrayList<>();

        Set<Integer> friends = objectSearchUser(userId).getFriends();

        for (Integer id : objectSearchUser(friendId).getFriends()) {
            if (friends.contains(id)) {
                users.add(objectSearchUser(id));
            }
        }
        log.info("Получение списка общих друзей пользовтеля {} с пользователем {}", userId, friendId);
        return users;
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
