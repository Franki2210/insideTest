package com.example.demo.token;

import com.example.demo.token.model.Token;
import com.example.demo.token.service.impl.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenTest {

    private final TokenService tokenService = new TokenService();

    @Test
    public void GenerateTokenTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.generateToken(null));

        Assertions.assertEquals(
                tokenService.generateToken(""),
                new Token("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiIn0.J_tAD_CGIs6BkpoZ3TWgWNV0mwGnIbGRoYLbfaZlERU")
        );
        Assertions.assertEquals(
                tokenService.generateToken("a"),
                new Token("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiYSJ9.hRx5Fs0Db_5jQ8eHtDc2VJCnbQDErrd_0KMApHGamCk")
        );
    }

    @Test
    public void ParseTokenTest() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.getFromToken(null));

        Assertions.assertThrows(IllegalArgumentException.class, () -> tokenService.getFromToken(""));

        Assertions.assertEquals(
                tokenService.getFromToken("eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiYSJ9.hRx5Fs0Db_5jQ8eHtDc2VJCnbQDErrd_0KMApHGamCk"),
                "a"
        );
    }
}
