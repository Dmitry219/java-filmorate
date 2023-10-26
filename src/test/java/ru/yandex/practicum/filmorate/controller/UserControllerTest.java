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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Validator;

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
    @Autowired
    UserController userController = new UserController();
    private Validator validator;

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
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-EmailCorrect.json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    void validateUserNegativeLoginNull() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-LoginNonNull.json")))
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-LoginBlank.json")))
                .andExpect(status().isBadRequest());

    }

    @Test
    void validateUserCopyOfLogin() throws Exception {
        User user = new User("mail@mail.ru","dolore");
        user.setId(1);
        user.setBirthday(LocalDate.of(1988,07,11));

        Assertions.assertNull(user.getName());

        userController.createUser(user);

        Assertions.assertEquals(user.getLogin(), user.getName(), "Лоигн не скопировался в имя!");
    }

    @Test
    void validateUserNegativeDateOfBirthInTheFuture() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.post(PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(getContentFromFilm("controller/request/user-InTheFuture.json")))
                .andExpect(status().isBadRequest());
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