package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private Validator validator = new Validator();

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //поставить лайк фильму
    @PutMapping(value = "/{id}/like/{userId}")
    public void addLikes(@PathVariable int id,@PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    //удалить лайк
    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLikes(@PathVariable int id,@PathVariable int userId) {
        checkId(id);
        checkId(userId);
        filmService.deleteLike(id, userId);
    }

    //вернуть список фильмов +
    @GetMapping
    public List<Film> getListFilms() {
        return filmService.getFilms();
    }

    //вернуть спиок количества филмов по популярности
    @GetMapping("/popular")
    public List<Film> getListFilmsPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    // вернуть фильм по ID +
    @GetMapping(value = "/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        checkId(filmId);
        return filmService.objectSearchFilm(filmId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) { //создание фильма
        log.info("Получили фильм {} ", film);
        validator.validate(film);
        film = filmService.createFilm(film);
        log.info("Получили фильм {} ", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление фильма
        checkId(film.getId());
        validator.validate(film);
        film = filmService.updateFilm(film);
        log.info("Обновили фильм {}", film);
            return film;
    }

    @DeleteMapping(value = "/{filmId}")
    public void deleteFilm(@PathVariable int filmId) { //удаление фильма
        filmService.deleteFilm(filmId);
    }

    private void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (filmService.objectSearchFilm(id).equals(null)) {
            throw new RuntimeException("Фильм с таким Id не сущетсует!");
        }
    }
}
