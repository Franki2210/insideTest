package com.example.demo.security;

import com.example.demo.exception.AuthException;
import com.example.demo.token.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class TokenChecker implements ITokenChecker {

    @Autowired
    private ITokenService tokenService;

    @Override
    public void checkBearerToken(HttpHeaders headers, String userName) throws AuthException {
        if (!headers.containsKey("Authorization")) {
            throw new AuthException();
        }

        String authorization = headers.getFirst("Authorization");
        if (authorization == null) {
            throw new AuthException();
        }

        String token = authorization.replace("Bearer ", "");
        String nameFromToken = tokenService.getFromToken(token);
        if (!nameFromToken.equals(userName)) {
            throw new AuthException();
        }
    }
}
