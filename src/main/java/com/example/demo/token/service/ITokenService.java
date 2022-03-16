package com.example.demo.token.service;

import com.example.demo.token.model.Token;

public interface ITokenService {
    Token generateToken(String username);

    String getFromToken(String token);
}
