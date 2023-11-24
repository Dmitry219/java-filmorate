package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenresDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
public class GenreService {
    private final GenresDbStorageImpl genresDbStorage;

    public GenreService(GenresDbStorageImpl genresDbStorage) {
        this.genresDbStorage = genresDbStorage;
    }

    public Genre getGenreById(int id) {
        return genresDbStorage.objectSearchGenre(id);
    }

    public List<Genre> getListGenres() {
        return genresDbStorage.getGenres();
    }
}
