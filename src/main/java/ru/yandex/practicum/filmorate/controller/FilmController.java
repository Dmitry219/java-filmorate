package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;
import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final Validator validator = new Validator();
    private final Map<Integer, Film> saveFilms = new HashMap<>();

    private int generateId = 0;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(saveFilms.values());
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) { //создание фильма
        validator.validate(film);
        film.setId(generateId());
        saveFilms.put(film.getId(), film);
        log.info("Получили фильм {} ", film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление фильма
        validator.validate(saveFilms.get(film.getId()));
        saveFilms.put(film.getId(), film);
        log.info("Обновили фильм {}", film);
            return film;
    }

    private int generateId() { //Генерация id
        return ++generateId;
    }
}
