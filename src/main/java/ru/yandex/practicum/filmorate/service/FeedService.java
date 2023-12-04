package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FeedDbStorageImpl;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

import java.time.LocalDateTime;

@Service
public class FeedService {
    private final FeedDbStorageImpl feedDbStorage;

    public FeedService(FeedDbStorageImpl feedDbStorage) {
        this.feedDbStorage = feedDbStorage;
    }

    //работа с лайками add delete update
    public void addLikeByUserEvent(int idUser, int idFilm){
        LocalDateTime currentDateTime = LocalDateTime.now();
        feedDbStorage.addLikeByUserEvent(currentDateTime,idUser, EventType.LIKE,
                Operation.ADD, idFilm);
    }
    //---------------
}
