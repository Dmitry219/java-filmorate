package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

@Data
public class Feed {
    int eventId;
    int userId;
    int entityId;
    EventType eventType;
    Operation operation;
    long timestamp;
}
