package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;
import ru.yandex.practicum.filmorate.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    public static final String PATH = "/users";
    @Autowired
    private MockMvc mockMvc;
    UserService userService;
    @Autowired
    UserController userController = new UserController(userService);
    private Validator validator;

    @Autowired
    public UserControllerTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    void createUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user.json")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/user.json")
                ));
    }

    @Test
    void createUserNegative() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-release-email-empty.json")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateUser() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/response/user.json")))
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.put(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-update-empty.json")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/user-update-empty.json")
                ));
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/userGet.json")))
                .andExpect(status().isOk());

        mockMvc.perform(
                        MockMvcRequestBuilders.get(PATH)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        getContentFromFilm("controller/response/userGet.json")
                ));
    }

    @Test
    void validateUserNegativeEmailNull() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-NonNull.json")))
                .andExpect(status().isNotFound());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-EmailCorrect.json")))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void validateUserNegativeLoginNull() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-LoginNonNull.json")))
                .andExpect(status().is4xxClientError());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-LoginBlank.json")))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void validateUserCopyOfLogin() {
        User user = new User("mail@mail.ru","dolore");
        user.setId(1);
        user.setBirthday(LocalDate.of(1988,07,11));

        Assertions.assertNull(user.getName());
        userService.createUser(user);

        Assertions.assertEquals(user.getLogin(), user.getName(), "Лоигн не скопировался в имя!");
    }

    @Test
    void validateUserNegativeDateOfBirthInTheFuture() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-InTheFuture.json")))
                .andExpect(status().is5xxServerError());
    }

    //--------------------Тесты нового функционала------------------------
    @Test
    void checkAddingFriends() {
        User user = new User("mail@mail.ru","dolore");
        user.setBirthday(LocalDate.of(1988,07,11));
        userService.createUser(user);

        User user1 = new User("mailNd@mail.ru","qqqqqqq");
        user.setBirthday(LocalDate.of(2000,11,11));
        userService.createUser(user1);

        userService.addFriends(user.getId(), user1.getId());

        Assertions.assertTrue(userService.objectSearchUser(user.getId()).getFriends().contains(user1.getId()),
                "Друг не добавилен!");
    }

    @Test
    void checkDeletionOfFriends() {
        User user = new User("mail@mail.ru","dolore");
        user.setBirthday(LocalDate.of(1988,07,11));
        userService.createUser(user);

        User user1 = new User("mailNd@mail.ru","qqqqqqq");
        user.setBirthday(LocalDate.of(2000,11,11));
        userService.createUser(user1);

        userService.addFriends(user.getId(), user1.getId());
        Assertions.assertTrue(userService.objectSearchUser(user.getId()).getFriends().contains(user1.getId()),
                "Друг не добавилен!");

        userService.deleteFriends(user.getId(), user1.getId());
        Assertions.assertFalse(userService.objectSearchUser(user.getId()).getFriends().contains(user1.getId()),
                "Друг не удалён!");
    }

    @Test
    void checkListOfPopularMovies() {
        User user = new User("mail@mail.ru","dolore");
        user.setBirthday(LocalDate.of(1988,07,11));
        userService.createUser(user);

        User user1 = new User("mailNd@mail.ru","qqqqqqq");
        user.setBirthday(LocalDate.of(2000,11,11));
        userService.createUser(user1);

        User user2 = new User("mailDs@mail.ru","wwwwww");
        user.setBirthday(LocalDate.of(1999,05,05));
        userService.createUser(user2);

        userService.addFriends(user.getId(), user1.getId());
        userService.addFriends(user.getId(), user2.getId());
        Assertions.assertTrue(userService.objectSearchUser(user.getId()).getFriends().contains(user1.getId()),
                "Друг не добавилен!");

        userService.getMutualFriends(user1.getId(), user2.getId());
        Assertions.assertEquals(user , userService.getMutualFriends(user2.getId(), user1.getId()).get(0),
                "Общие друзья не найдены!");
    }

    private String getContentFromFilm(String filename) {
        try {
            return Files.readString(ResourceUtils.getFile("classpath:" + filename).toPath(),
                    StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}