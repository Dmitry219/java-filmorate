package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

@Data
public class Feed {
    private int eventId;
    private int userId;
    private int entityId;
    private EventType eventType;
    private Operation operation;
    private long timestamp;
}
