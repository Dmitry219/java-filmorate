package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecommendationService {
    private final JdbcTemplate jdbcTemplate;
    private final FilmService filmService;
    private final UserService userService;

    public List<Film> getRecommendation(Integer id) {
        userService.checkId(id);
        List<Like> allLikes = jdbcTemplate.query("SELECT * FROM likes", this::rsToLike);
        Map<Integer, List<Integer>> usersAndLikedFilmsIds = getMapOfUsersAndLikes(allLikes);
        List<Integer> listOfRecommendedFilmsIds = getRecommendationsList(usersAndLikedFilmsIds, id);
        if (listOfRecommendedFilmsIds.size() < 1) {
            return Collections.emptyList();
        }
        return listOfRecommendedFilmsIds.stream()
                .map(filmService::objectSearchFilm)
                .collect(Collectors.toList());
    }

    private List<Integer> getRecommendationsList(
            Map<Integer, List<Integer>> usersAndLikedFilmsIds,
            int userId) {
        List<Integer> userLikedFilmsIds = usersAndLikedFilmsIds.get(userId);
        Map<Integer, Long> idsAndMatchesCount = new HashMap<>();
        for (int otherUserId : usersAndLikedFilmsIds.keySet()) {
            if (otherUserId == userId) {
                continue;
            }
            List<Integer> userLikes = new ArrayList<>(userLikedFilmsIds);
            userLikes.retainAll(usersAndLikedFilmsIds.get(otherUserId));
            if (userLikes.isEmpty()) {
                continue;
            }
            long commonLikesCount = userLikes.size();
            idsAndMatchesCount.put(otherUserId, commonLikesCount);
        }

        if (idsAndMatchesCount.isEmpty()) {
            return Collections.emptyList();
        }
        List<Integer> topTenMatchesUsersIds = idsAndMatchesCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return topTenMatchesUsersIds.stream()
                .map(usersAndLikedFilmsIds::get)
                .flatMap(List::stream)
                .filter(filmId -> !userLikedFilmsIds.contains(filmId))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Integer>> getMapOfUsersAndLikes(List<Like> allLikes) {
        return allLikes.stream()
                .collect(Collectors.toMap(
                        Like::getUserId,
                        like -> List.of(like.getFilmId()),
                        this::concatenate
                ));
    }

    private <T> List<T> concatenate(List<T> first, List<T> second) {
        List<T> result = new ArrayList<>(first);
        result.addAll(second);
        return result;
    }

    private Like rsToLike(ResultSet rs, int rowNum) throws SQLException {
        int filmId = rs.getInt("id_film");
        int userId = rs.getInt("id_user");
        return Like.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
    }
}
