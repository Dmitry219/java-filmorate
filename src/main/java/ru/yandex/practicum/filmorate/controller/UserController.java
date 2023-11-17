package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //список всех users
    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    //вернуть user по id
    @GetMapping(value = "/{userId}")
    public User getUserById(@PathVariable int userId) {
        checkId(userId);
        return userService.objectSearchUser(userId);
    }

    //добавление в друзья
    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        checkId(id);
        checkId(friendId);
        userService.addFriends(id, friendId);
    }

    //удаление из друзей
    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        checkId(id);
        checkId(friendId);
        userService.deleteFriends(id, friendId);
    }

    //возвращаем список пользователей, являющихся его друзьями.
    @GetMapping(value = "/{id}/friends")
    public List<User> getOfFriendsOfASpecificUser(@PathVariable int id) {
        checkId(id);
        return userService.getOfFriendsOfASpecificUser(id);
    }

    //список друзей, общих с другим пользователем
    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getListOfFriendsSharedWithAnotherUser(@PathVariable int id,
                                                             @PathVariable int otherId) {
        checkId(id);
        checkId(otherId);
        return userService.getMutualFriends(id, otherId);
    }

    //создание пользователя
    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        log.info("Получили пользователя {}", user);
        return user;
    }

    //обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        checkId(user.getId());
        userService.updateUser(user);
        log.info("Обновили пользователя {}", user);
        return user;
    }

    //удаление user
    @DeleteMapping(value = "/{userId}")
    public void deleteFilm(@PathVariable int userId) {
        checkId(userId);

        userService.deleteUser(userId);
    }

    //проверка id  ( > 0) и (user существует с такми id)
    private void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (userService.objectSearchUser(id).equals(null)) {
            throw new RuntimeException("Пользователь с таким Id не сущетсует!");
        }
    }

}
