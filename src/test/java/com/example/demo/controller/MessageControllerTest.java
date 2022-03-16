package com.example.demo.controller;

import com.example.demo.message.dao.MessageDao;
import com.example.demo.message.dto.MessageDto;
import com.example.demo.token.model.Token;
import com.example.demo.token.service.impl.TokenService;
import com.example.demo.user.dao.UserDao;
import com.example.demo.user.dto.UserDto;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Helpers.getRandomMessagesFromUser;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private MessageDao messageDao;

    private static UserDto testUser;
    private static UserDto testUser2;
    private static MessageDto testMessage1;
    private final static String TEST_MESSAGE = "testMessage";
    private static String testMessageDataForUser1;
    private static String testMessageDataHistory;


    @BeforeAll
    public static void InitTest() throws JSONException {
        testUser = new UserDto("user1", "pass1");
        testUser.setId(1);
        testUser2 = new UserDto("user2", "pass2");
        testUser2.setId(2);

        testMessage1 = new MessageDto(testUser.getId(), TEST_MESSAGE);

        testMessageDataForUser1 = getRequestData(testUser.getName(), testMessage1.getText());
        testMessageDataHistory = getRequestData(testUser.getName(), "history 10");
    }

    private static String getRequestData(String name, String message) throws JSONException {
        return new JSONObject()
                .put("name", name)
                .put("message", message)
                .toString();
    }

    //С токеном совпадающим с именем
    @Test
    public void correct() throws Exception {
        when(messageDao.save(testMessage1)).thenReturn(testMessage1);
        when(userDao.getByName(testUser.getName())).thenReturn(testUser);

        Token tokenForTestUser = tokenService.generateToken(testUser.getName());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/message")
                        .content(testMessageDataForUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenForTestUser.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$", hasItem(testMessage1.getText())));
    }

    //Без токена
    @Test
    public void withoutToken() throws Exception {
        when(userDao.getByName(testUser.getName())).thenReturn(testUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/message")
                        .content(testMessageDataForUser1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    //С токеном от другого пользователя
    @Test
    public void withOtherToken() throws Exception {
        when(userDao.getByName(testUser.getName())).thenReturn(testUser);

        String tokenForTestUser2 = tokenService.generateToken(testUser2.getName()).getToken();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/message")
                        .content(testMessageDataForUser1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenForTestUser2))
                .andExpect(status().isForbidden());
    }

    //Получение последних сообщений
    @Test
    public void history10() throws Exception {
        int countMessages = 10;
        List<MessageDto> messages = getRandomMessagesFromUser(countMessages, testUser.getId());

        when(messageDao.findByUserIdOrderByIdDesc(testUser.getId(), PageRequest.of(0, 10)))
                .thenReturn(messages);
        when(userDao.getByName(testUser.getName())).thenReturn(testUser);

        Token tokenForTestUser = tokenService.generateToken(testUser.getName());

        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                        .post("/message")
                        .content(testMessageDataHistory)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenForTestUser.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(countMessages)));
        for (MessageDto messageDto : messages) {
            actions.andExpect(jsonPath("$", hasItem(messageDto.getText())));
        }
    }

    //Получение последних сообщений, если сообщений нет
    @Test
    public void history10WhenEmpty() throws Exception {
        List<MessageDto> messages = new ArrayList<>();

        when(messageDao.findByUserIdOrderByIdDesc(testUser.getId(), PageRequest.of(0, 10)))
                .thenReturn(messages);
        when(userDao.getByName(testUser.getName())).thenReturn(testUser);

        Token tokenForTestUser = tokenService.generateToken(testUser.getName());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/message")
                        .content(testMessageDataHistory)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + tokenForTestUser.getToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
