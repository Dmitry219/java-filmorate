package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FriendshipDbStorageImpl;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendshipDbStorageImpl friendshipDbStorage;
    private final Validator validator = new Validator();


    @Autowired
    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendshipDbStorageImpl friendshipDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }

    public void addFriends(int userId, int friendId) {
       //objectSearchUser(userId).addFriends(friendId);//у user появляется друг friend
        //objectSearchUser(friendId).addFriends(userId);//у friend появляется друг user
        checkId(userId);
        checkId(friendId);
        friendshipDbStorage.addFriends(userId, friendId);
        log.info("Добавление в друзья к пользователю {} добавляемый пользователь {}", userId, friendId);
    }

    public void deleteFriends(int userId, int friendId) {
        //objectSearchUser(userId).deleteFriends(friendId);//у user удалить друг friend
        //objectSearchUser(friendId).deleteFriends(userId);//у friend удалить друг user
        checkId(userId);
        checkId(friendId);
        friendshipDbStorage.deleteFriendByUserId(userId, friendId);
        log.info("Добавление в друзья к пользователю {} добавляемый пользователь {}", userId, friendId);
    }

    //получить друзей конкретонго пользовятеля
    public List<User> getOfFriendsOfASpecificUser(int userId) {
        log.info("Получение в UserService id {} пользователя", userId);
//        List<User> friends = new ArrayList<>();
//        for (Integer id : objectSearchUser(userId).getFriends()) {
//            friends.add(objectSearchUser(id));
//            log.info("Пользователь {}", objectSearchUser(id));
//        }
//        log.info("Получение списка друзей пользователя {}", userId);
        checkId(userId);
        return friendshipDbStorage.getFriendByUserId(userId);
    }

    public List<User> getMutualFriends(int userId, int friendId) {

//        List<User> users = new ArrayList<>();
//
//        Set<Integer> friends = objectSearchUser(userId).getFriends();
//
//        for (Integer id : objectSearchUser(friendId).getFriends()) {
//            if (friends.contains(id)) {
//                users.add(objectSearchUser(id));
//            }
//        }
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

    public void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("UserId не может быть меньше нуля или равен нулю!");
        } else if (userStorage.objectSearchUser(id).equals(null)) {
            throw new RuntimeException("Пользователь с таким Id не существует!");
        }
    }
}
