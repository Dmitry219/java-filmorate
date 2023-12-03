package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorageImpl mpaDbStorage;

    public MpaService(MpaDbStorageImpl mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa getMpaById(int mpaId) {
        return mpaDbStorage.objectSearchMpa(mpaId);
    }

    public List<Mpa> getListMpa() {
        return mpaDbStorage.getMpa();
    }
}
