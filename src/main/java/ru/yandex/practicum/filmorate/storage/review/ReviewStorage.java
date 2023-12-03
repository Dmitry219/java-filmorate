package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;
import java.util.List;

public interface ReviewStorage {
    Review create(Review review);

    Review update(Review review);

    void delete(int id);

    Review getById(int id);

    boolean isExistById(int id);

    List<Review> getFewByFilmIdByCount(int filmId, int count);

    List<Review> getFewByCount(int count);

    void addLike(int id, int userId);

    void addDislike(int id, int userId);

    void deleteLike(int id, int userId);

    void deleteDislike(int id, int userId);
}
