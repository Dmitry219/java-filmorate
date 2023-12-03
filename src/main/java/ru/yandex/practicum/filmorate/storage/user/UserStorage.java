package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(int userId);

    public User objectSearchUser(int userId);

    public List<User> getUsers();
}
