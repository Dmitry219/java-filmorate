package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    private int id;
    @NotNull
    @NotBlank
    private String name;

    public Map<String, Object> toMap() {
        Map<String, Object> directorMap = new HashMap<>();
        directorMap.put("name", name);
        return directorMap;
    }
}
