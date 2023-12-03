package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    Review create(@Valid @RequestBody Review review) {
        log.info("Post /reviews добавление нового отзыва {}", review);
        return reviewService.create(review);
    }

    @PutMapping()
    Review update(@Valid @RequestBody Review review) {
        log.info("PUT /reviews редактирование уже имеющегося отзыва {}", review);
        return reviewService.update(review);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable int id) {
        log.info("DELETE /reviews/{} Удаление уже имеющегося отзыва", id);
        reviewService.delete(id);
    }

    @GetMapping("/{id}")
    Review getById(@PathVariable int id) {
        log.info("GET /reviews/{} Получение отзыва по идентификатору", id);
        return reviewService.getById(id);
    }

    @GetMapping("")
    List<Review> getReviewsByFilmIdByCount(@RequestParam (required = false) Integer filmId,
                                       @RequestParam (required = false,
                                               defaultValue = "10") Integer count) {
        log.info("GET /reviews?filmId={}&count={} Получение всех отзывов по идентификатору фильма," +
                " если фильм не указан то все. Если кол-во не указано то 10", filmId, count);
        return reviewService.getReviewsByFilmIdByCount(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    void addLike(@PathVariable int id,
                   @PathVariable int userId) {
        log.info("PUT /reviews/{}/like/{} пользователь ставит лайк отзыву", id, userId);
        reviewService.addLike(id, userId);
    }

    @PutMapping("/{id}/dislike/{userId}")
    void addDisLike(@PathVariable int id,
                   @PathVariable int userId) {
        log.info("PUT /reviews/{}/dislike/{} пользователь ставит дизлайк отзыву", id, userId);
        reviewService.addDisLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void deleteLike(@PathVariable int id,
                      @PathVariable int userId) {
        log.info("DELETE /reviews/{}/like/{} пользователь удаляет лайк отзыву", id, userId);
        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    void deleteDisLike(@PathVariable int id,
                    @PathVariable int userId) {
        log.info("DELETE /reviews/{}/dislike/{} пользователь удаляет дизлайк отзыву", id, userId);
        reviewService.deleteDisLike(id, userId);
    }
}
