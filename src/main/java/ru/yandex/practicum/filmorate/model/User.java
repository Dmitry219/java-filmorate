package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть в будущем времени!")
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriends(Integer id) {
        friends.remove(id);
    }

}
