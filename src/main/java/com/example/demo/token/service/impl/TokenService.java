package com.example.demo.token.service.impl;

import com.example.demo.token.model.Token;
import com.example.demo.token.service.ITokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;

@Service
public class TokenService implements ITokenService {

    private static final Key KEY = Keys
            .hmacShaKeyFor("0CB4A74A60D63D54D4C4A2193481BBC8B5D54AFC2D0922F90611F14BC086E37A"
                .getBytes(StandardCharsets.UTF_8)
            );

    @Override
    public Token generateToken(String username) {

        if (username == null)
            throw new IllegalArgumentException("Username must not be null when generating token");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim("name", username)
                .signWith(KEY, SignatureAlgorithm.HS256);
        return new Token(jwtBuilder.compact());
    }

    @Override
    public String getFromToken(String token) {
        if (token == null)
            throw new IllegalArgumentException("Token must not be null");
        if (token.equals(""))
            throw new IllegalArgumentException("Token must not be empty");

        Jws<Claims> a = Jwts.parserBuilder().setSigningKey(KEY).build().parseClaimsJws(token);
        return (String) a.getBody().get("name");
    }

}
