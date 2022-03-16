package com.example.demo.auth;

import com.example.demo.auth.facade.IAuthFacade;
import com.example.demo.exception.AuthException;
import com.example.demo.token.model.Token;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthFacade authFacade;

    @Autowired
    public AuthController(IAuthFacade authFacade) {
        this.authFacade = authFacade;
    }

    @PostMapping
    @ResponseBody
    public Token auth(@RequestBody User user) throws AuthException {
        return authFacade.auth(user);
    }
}
