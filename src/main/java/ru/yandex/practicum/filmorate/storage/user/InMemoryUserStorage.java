package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Validator validator = new Validator();
    private final Map<Integer, User> saveUsers = new HashMap<>();
    private int generateId = 0;


    @Override
    public User createUser(User user) {
        user = validator.validate(user);
        user.setId(generateId());
        saveUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validator.validate(saveUsers.get(user.getId()));
        saveUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        saveUsers.remove(userId);
    }

    @Override
    public User objectSearchUser(int userId) {
        return saveUsers.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(saveUsers.values());
    }

    private int generateId() { //Генерация id
        return ++generateId;
    }
}
