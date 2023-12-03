package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmMapper implements RowMapper<Film> {
    private final MpaDbStorageImpl mpaDbStorage;
    private final GenresDbStorageImpl genresDbStorage;
    private final DirectorDbStorageImpl directorDbStorage;

    @Autowired
    public FilmMapper(MpaDbStorageImpl mpaDbStorage, GenresDbStorageImpl genresDbStorage,
                      DirectorDbStorageImpl directorDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
        this.genresDbStorage = genresDbStorage;
        this.directorDbStorage = directorDbStorage;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_Date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(mpaDbStorage.objectSearchMpa(rs.getInt("id_MPA")));
        film.setGenres(genresDbStorage.getGenresByFilmId(rs.getInt("id")));
        film.setDirectors(directorDbStorage.getDirectorByFilmId(rs.getInt("id")));

        return film;
    }
}
