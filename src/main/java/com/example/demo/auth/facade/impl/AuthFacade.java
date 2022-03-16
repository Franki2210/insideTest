package com.example.demo.auth.facade.impl;

import com.example.demo.auth.facade.IAuthFacade;
import com.example.demo.exception.AuthException;
import com.example.demo.token.model.Token;
import com.example.demo.token.service.ITokenService;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.model.User;
import com.example.demo.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade implements IAuthFacade {

    private final IUserService userService;

    private final ITokenService tokenService;

    @Autowired
    public AuthFacade(IUserService userService, ITokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public Token auth(User user) throws AuthException {
        UserDto userDto = userService.getByName(user.getName());
        if (userDto == null || !user.getPassword().equals(userDto.getPassword())) {
            throw new AuthException();
        }
        return tokenService.generateToken(user.getName());
    }
}
