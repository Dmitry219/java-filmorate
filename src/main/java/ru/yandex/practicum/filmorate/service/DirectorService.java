package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.DirectorDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class DirectorService {
    private final DirectorDbStorageImpl directorDbStorage;

    public DirectorService(DirectorDbStorageImpl directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }

    public Director createDirector(Director director) {
        log.info("Создание режисёра {}", director);
        return directorDbStorage.createDirector(director);
    }

    //венуть режисора по id
    public Director getDirectorById(int id) {
        return directorDbStorage.objectSearchDirector(id);
    }
    //венуть всех режисоров
    public List<Director> getListDirectors() {
        return directorDbStorage.getDirectors();
    }
}
