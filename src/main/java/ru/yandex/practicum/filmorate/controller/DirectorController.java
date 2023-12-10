package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;
    private final Validator validator;

    @PostMapping
    public Director createDirector(@Valid @RequestBody Director director) { //создание режисёра
        log.info("Получили режисёра {} ", director);
        director = directorService.createDirector(director);
        log.info("Вернулся режисёр после создания {} ", director);
        return director;
    }

    @PutMapping
    public Director updateDirector(@Valid @RequestBody Director director) { //обновление режисёра
        checkId(director.getId());
        director = directorService.updateDirector(director);
        log.info("Обновили режисёра {}", director);
        return director;
    }

    @GetMapping
    public List<Director> getListDirector() {
        return directorService.getListDirectors();
    }

    @GetMapping(value = "/{id}")
    public Director getDirectorById(@PathVariable int id) {
        checkId(id);
        return directorService.getDirectorById(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteDirector(@PathVariable int id) { //удаление режисёра
        directorService.deleteDirector(id);
    }

    private void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (directorService.getDirectorById(id).equals(null)) {
            throw new RuntimeException("Режисёр с таким Id не сущетсует!");
        }
    }
}
