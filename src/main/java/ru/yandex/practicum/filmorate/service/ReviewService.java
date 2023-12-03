package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private FilmService filmService;
    private UserService userService;

    public Review create(Review review) {
        isReviewExist(review);
        checkIds(review);
        return reviewStorage.create(review);
    }

    public Review update(Review review) {
        isReviewExist(review);
        checkReviewId(review.getReviewId());
        checkIds(review);
        return reviewStorage.update(review);
    }

    public void delete(int id) {
        checkReviewId(id);
        reviewStorage.delete(id);
    }

    public Review getById(int id) {
        checkReviewId(id);
        return reviewStorage.getById(id);
    }

    public List<Review> getReviewsByFilmIdByCount(Integer filmId, Integer count) {
        if (count == null) count = 10;
        if (filmId == null) {
            return reviewStorage.getFewByCount(count);
        }
        return reviewStorage.getFewByFilmIdByCount(filmId, count);
    }

    public void addLike(int id, int userId) {
        reviewStorage.addLike(id, userId);
    }

    public void addDisLike(int id, int userId) {
        reviewStorage.addDislike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        reviewStorage.deleteLike(id, userId);
    }

    public void deleteDisLike(int id, int userId) {
        reviewStorage.deleteDislike(id, userId);
    }

    private void isReviewExist(Review review) {
        if (review == null) {
            throw new ValidationException("review не должен быть null!");
        }
    }

    private void checkReviewId(int id) {
        if (!reviewStorage.isExistById(id)) {
            throw new RuntimeException("review с id = " + id + " не найден!");
        }
    }

    private void checkIds(Review review) {
        if (review.getFilmId() == 0) {
            throw new ValidationException("Film Id не должен быть равен нулю!");
        }
        if (review.getUserId() == 0) {
            throw new ValidationException("User Id не должен быть равен нулю!");
        }
        filmService.checkId(review.getFilmId());
        userService.checkId(review.getUserId());
    }
}
