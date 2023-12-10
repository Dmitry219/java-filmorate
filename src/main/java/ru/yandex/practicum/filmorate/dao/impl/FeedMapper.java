package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enumFeed.EventType;
import ru.yandex.practicum.filmorate.model.enumFeed.Operation;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FeedMapper implements RowMapper<Feed> {

    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();

        feed.setTimestamp(rs.getTimestamp("timestamp").getTime());
        feed.setUserId(rs.getInt("userId"));
        feed.setEventType(EventType.valueOf(rs.getString("eventType")));
        feed.setOperation(Operation.valueOf(rs.getString("operation")));
        feed.setEventId(rs.getInt("eventId"));
        feed.setEntityId(rs.getInt("entityId"));

        return feed;
    }
}
