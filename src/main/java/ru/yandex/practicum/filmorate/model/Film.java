package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

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
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();

    public Map<String, Object> toMap() {
        Map<String, Object> filmMap = new HashMap<>();
        filmMap.put("name", name);
        filmMap.put("description", description);
        filmMap.put("release_Date", releaseDate);
        filmMap.put("duration", duration);
        filmMap.put("id_MPA", mpa.getId());

        return filmMap;
    }

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
