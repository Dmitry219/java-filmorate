package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
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

    public Map<String, Object> toMap(){
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("login", login);
        userMap.put("name", name);
        userMap.put("birthday", birthday);

        return userMap;
    }

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriends(Integer id) {
        friends.remove(id);
    }

}
