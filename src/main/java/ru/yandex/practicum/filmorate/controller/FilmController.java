package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    //поставить лайк фильму
    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLikes(@PathVariable int id,@PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    //удалить лайк
    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikes(@PathVariable int id,@PathVariable int userId) {
        checkId(id);
        checkId(userId);
        filmService.deleteLike(id, userId);
    }

    //вернуть список фильмов
    @GetMapping("/films")
    public List<Film> getListFilms() {
        return filmService.getFilms();
    }

    //вернуть спиок количества филмов по популярности
    @GetMapping("/films/popular")
    public List<Film> getListFilmsPopular(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    // вернуть фильм по ID
    @GetMapping(value = "/films/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        checkId(filmId);
        return filmService.objectSearchFilm(filmId);
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) { //создание фильма
        film = filmService.createFilm(film);
        log.info("Получили фильм {} ", film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление фильма
        checkId(film.getId());
        film = filmService.updateFilm(film);
        log.info("Обновили фильм {}", film);
            return film;
    }

    @DeleteMapping(value = "/films/{filmId}")
    public void deleteFilm(@PathVariable int filmId) { //удаление фильма
        filmService.deleteFilm(filmId);
    }

    public void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (filmService.objectSearchFilm(id).equals(null)) {
            throw new RuntimeException("Фильм с таким Id не сущетсует!");
        }
    }
}
