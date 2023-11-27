package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@Component
public class MpaController {
    private final MpaService mpaService;
    private final Validator validator;

    @Autowired
    public MpaController(MpaService mpaService, Validator validator) {
        this.mpaService = mpaService;
        this.validator = validator;
    }

    @GetMapping(value = "/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        checkId(id);
        return mpaService.getMpaById(id);
    }

    @GetMapping
    public List<Mpa> getListMpa() {
        return mpaService.getListMpa();
    }

    private void checkId(int id) {
        if (id <= 0) {
            throw new RuntimeException("Id не может быть меньше нуля или равен нулю!");
        } else if (mpaService.getMpaById(id).equals(null)) {
            throw new RuntimeException("Фильм с таким Id не сущетсует!");
        }
    }
}
