package com.example.demo.auth.facade;

import com.example.demo.exception.AuthException;
import com.example.demo.token.model.Token;
import com.example.demo.user.model.User;

public interface IAuthFacade {
    Token auth(User user) throws AuthException;
}
