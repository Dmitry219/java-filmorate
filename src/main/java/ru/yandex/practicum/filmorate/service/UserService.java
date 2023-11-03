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
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriends(int userId, int friendId) {
        objectSearchUser(userId).addFriends(friendId);//у user появляется друг friend
        objectSearchUser(friendId).addFriends(userId);//у friend появляется друг user
    }

    public void deleteFriends(int userId, int friendId) {
        objectSearchUser(userId).deleteFriends(friendId);//у user удалить друг friend
        objectSearchUser(friendId).deleteFriends(userId);//у friend удалить друг user
    }

    //получить друзей конкретонго пользовятеля
    public List<User> getOfFriendsOfASpecificUser(int userId) {
        List<User> friends = new ArrayList<>();
        for (Integer id : objectSearchUser(userId).getFriends()) {
            friends.add(objectSearchUser(id));
            log.info("Юзер {}", objectSearchUser(id));
        }
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
        return users;
    }

    //------------------методы UserStorage-------------------
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }

    public User objectSearchUser(int userId) {
        return userStorage.objectSearchUser(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}
