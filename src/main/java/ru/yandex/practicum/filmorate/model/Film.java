package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200, message = "Описание превышает лемита символов!")
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не должная быть меньше нуля!")
    private int duration;
    private Set<Integer> likes = new HashSet<>();

    public int getLikesCount() {
        return likes.size();
    }

    public void addLikes(Integer userId) {
        likes.add(userId);
    }

    public void deleteLikes(Integer userId) {
        likes.remove(userId);
    }
}
