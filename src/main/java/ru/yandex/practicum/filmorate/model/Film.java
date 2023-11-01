package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

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
}
