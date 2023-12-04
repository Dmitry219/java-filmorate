package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Data
public class Feed {
    LocalDateTime timesTamp;
    int userId;
    EventType eventType;
    Operation operation;
    int eventId;
    int entityId;
}
