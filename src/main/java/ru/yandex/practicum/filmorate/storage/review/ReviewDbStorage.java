package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    private Review mapRowToReview(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getInt("id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("is_positive"))
                .userId(resultSet.getInt("user_id"))
                .filmId(resultSet.getInt("film_id"))
                .useful(resultSet.getInt("useful"))
                .build();
    }

    @Override
    public Review create(Review review) {
        String sql = "INSERT INTO reviews (content, is_Positive, user_id, film_id)" +
                " VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, review.getContent());
            stmt.setBoolean(2, review.getIsPositive());
            stmt.setInt(3, review.getUserId());
            stmt.setInt(4, review.getFilmId());
            return stmt;
        }, keyHolder);
        review.setReviewId(keyHolder.getKey().intValue());
        log.info("добавлен отзыв - {}", review);
        return review;
    }

    @Override
    public Review update(Review review) {
        String sql = "UPDATE reviews " +
                "SET content = ?, is_Positive =? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        Review updatedReview = getById(review.getReviewId());
        log.info("обновлен отзыв - {}", updatedReview);
        return updatedReview;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        jdbcTemplate.update(sql, id);
        log.info("удален отзыв с id - {}", id);
    }

    @Override
    public Review getById(int id) {
        String sql = "SELECT r.id, r.content, r.is_positive, r.user_id, r.film_id, " +
                "IFNULL(SUM(rl.rating), 0) AS useful " +
                "FROM REVIEWS r " +
                "LEFT JOIN REVIEW_LIKES rl on r.id = rl.review_id " +
                "WHERE r.id = ? " +
                "GROUP BY r.id ";
        Review review = jdbcTemplate.queryForObject(sql, this::mapRowToReview, id);
        log.info("получен отзыв {}", review);
        return review;
    }

    @Override
    public boolean isExistById(int id) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT 1 FROM reviews WHERE id = ?)", Boolean.class, id);
    }

    @Override
    public List<Review> getFewByFilmIdByCount(int filmId, int count) {
        String sql = "SELECT r.id, r.content, r.is_positive, r.user_id, r.film_id, " +
                "IFNULL(SUM(rl.rating), 0) AS useful " +
                "FROM REVIEWS r " +
                "LEFT JOIN REVIEW_LIKES rl on r.id = rl.review_id " +
                "WHERE r.film_id = ? " +
                "GROUP BY r.id " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        List<Review> reviewList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToReview, filmId, count))
                .orElse(Collections.emptyList());
        log.info("получены отзывы для фильма с id {} : {} ", filmId, reviewList);
        return reviewList;
    }

    @Override
    public List<Review> getFewByCount(int count) {
        String sql = "SELECT r.id, r.content, r.is_positive, r.user_id, r.film_id, " +
                "IFNULL(SUM(rl.rating), 0) AS useful " +
                "FROM REVIEWS r " +
                "LEFT JOIN REVIEW_LIKES rl on r.id = rl.review_id " +
                "GROUP BY r.id " +
                "ORDER BY useful DESC " +
                "LIMIT ?";
        List<Review> reviewList = Optional.ofNullable(jdbcTemplate.query(sql, this::mapRowToReview, count))
                .orElse(Collections.emptyList());
        log.info("получены отзывы для всех фильмов : {} ", reviewList);
        return reviewList;
    }

    @Override
    public void addLike(int id, int userId) {
        String sql = "MERGE INTO review_likes AS rl " +
                "USING (SELECT " + id + " AS review_id, " + userId + " AS user_id) " +
                "AS t ON rl.review_id = t.review_id AND rl.review_id = t.user_id " +
                "WHEN NOT MATCHED THEN " +
                "   INSERT VALUES(?, ?, 1) " +
                "WHEN MATCHED THEN " +
                "   UPDATE SET rl.rating = 1 ";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void addDislike(int id, int userId) {
        String sql = "MERGE INTO review_likes AS rl " +
                "USING (SELECT " + id + " AS review_id, " + userId + " AS user_id) " +
                "AS t ON rl.review_id = t.review_id AND rl.review_id = t.user_id " +
                "WHEN NOT MATCHED THEN " +
                "   INSERT VALUES(?, ?, -1) " +
                "WHEN MATCHED THEN " +
                "   UPDATE SET rl.rating = -1 ";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ? AND rating = 1";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void deleteDislike(int id, int userId) {
        String sql = "DELETE FROM review_likes WHERE review_id = ? AND user_id = ? AND rating = -1";
        jdbcTemplate.update(sql, id, userId);
    }
}
