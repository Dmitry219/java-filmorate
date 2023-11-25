package ru.yandex.practicum.filmorate.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;

@Slf4j
@Component
public class Validator {
    private static final LocalDate DATE_BORDER = LocalDate.of(1895,12,28);

    public void validate(Film film) {
        if (film == null) {
            throw new ValidationException("film  не найде по id!");
        } else if (film.getReleaseDate().isBefore(DATE_BORDER)) {
            log.info("У фильмы неправильная дата");
            throw new ValidationException("Старая дата релиза фильма!");
        }
    }

    public User validate(User user) {
        if (user == null) {
            throw new ValidationException("user не найде по id!");
        } else if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return user;
    }
}
