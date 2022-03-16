package com.example.demo.controller;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private UserDao userDao;

    private static UserDto testUser1;

    @BeforeAll
    public static void InitTest() throws JSONException {
        testUser1 = new UserDto("user1", "pass1");
        testUser1.setId(1);
    }

    @Test
    public void correctAuthData() throws Exception {
        when(userDao.getByName(testUser1.getName())).thenReturn(testUser1);
        Token tokenForTestUser = tokenService.generateToken(testUser1.getName());

        String correctAuthData = new JSONObject()
                .put("name", testUser1.getName())
                .put("password", testUser1.getPassword())
                .toString();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content(correctAuthData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token", is(tokenForTestUser.getToken())));
    }

    @Test
    public void unknownUsername() throws Exception {
        when(userDao.getByName(testUser1.getName())).thenReturn(testUser1);

        String failPasswordAuthData = new JSONObject()
                .put("name", "unknownName")
                .put("password", testUser1.getPassword())
                .toString();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content(failPasswordAuthData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void failPassword() throws Exception {
        when(userDao.getByName(testUser1.getName())).thenReturn(testUser1);

        String failPasswordAuthData = new JSONObject()
                .put("name", testUser1.getName())
                .put("password", "failPassword")
                .toString();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content(failPasswordAuthData)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void emptyData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
