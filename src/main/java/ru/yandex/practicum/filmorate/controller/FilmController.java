package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@Component
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedListFilms(@PathVariable int directorId,
                                         @RequestParam String sortBy) {
        return filmService.getSortedFilms(directorId, sortBy);
    }

    //поставить лайк фильму
    @PutMapping(value = "/{id}/like/{userId}")
    public void addLikes(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    //удалить лайк
    @DeleteMapping(value = "/{id}/like/{userId}")
    public void deleteLikes(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    //вернуть список фильмов +
    @GetMapping
    public List<Film> getListFilms() {
        return filmService.getFilms();
    }

    //вернуть спиок количества филмов по популярности
    @GetMapping("/popular")
    public List<Film> getPopularFilmsByGenreAndYear(@RequestParam(defaultValue = "20") int count,
                                                    @RequestParam(required = false) Integer genreId,
                                                    @RequestParam(required = false) Integer year) {
        return filmService.getPopularFilmsByGenreAndYear(count, genreId, year);
    }

    // вернуть фильм по ID +
    @GetMapping(value = "/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        return filmService.objectSearchFilm(filmId);
    }

    //получение общих фильмов
    @GetMapping(value = "/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) { //создание фильма
        log.info("Получили фильм {} ", film);
        film = filmService.createFilm(film);
        log.info("Получили фильм {} ", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { //обновление фильма
        film = filmService.updateFilm(film);
        log.info("Обновили фильм {}", film);
        return film;
    }

    @DeleteMapping(value = "/{filmId}")
    public void deleteFilm(@PathVariable int filmId) { //удаление фильма
        filmService.deleteFilm(filmId);
    }
}

