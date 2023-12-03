package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DirectorMapper implements RowMapper<Director> {
    private final DirectorDbStorageImpl directorDbStorage;


    public DirectorMapper(DirectorDbStorageImpl directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }

    @Override
    public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
        Director director = new Director();
        director.setId(rs.getInt("id"));
        director.setName(rs.getString("name"));

        return director;
    }
}
