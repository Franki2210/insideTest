package com.example.demo.security;

import com.example.demo.exception.AuthException;
import org.springframework.http.HttpHeaders;

public interface ITokenChecker {

    void checkBearerToken(HttpHeaders headers, String userName) throws AuthException;
}
