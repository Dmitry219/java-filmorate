package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Validator validator = new Validator();
    private final Map<Integer, User> saveUsers = new HashMap<>();
    private int generateId = 0;

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(saveUsers.values());
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) { //создание пользователя
        user = validator.validate(user);
        user.setId(generateId());
        saveUsers.put(user.getId(), user);
        log.info("Получили пользователя {}", user);
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) { //обновление пользователя
        validator.validate(saveUsers.get(user.getId()));
        saveUsers.put(user.getId(), user);
        log.info("Обновили пользователя {}", user);
        return user;
    }

    private int generateId() { //Генерация id
        return ++generateId;
    }
}
