package com.example.demo.message;

import com.example.demo.exception.AuthException;
import com.example.demo.message.facade.IMessageFacade;
import com.example.demo.message.model.UserMessage;
import com.example.demo.security.ITokenChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final IMessageFacade messageFacade;
    private final ITokenChecker tokenChecker;

    @Autowired
    public MessageController(IMessageFacade messageFacade, ITokenChecker tokenChecker) {
        this.messageFacade = messageFacade;
        this.tokenChecker = tokenChecker;
    }

    @PostMapping
    @ResponseBody
    public List<String> getMessage(@RequestHeader HttpHeaders headers,
                                   @RequestBody UserMessage userMessage) throws AuthException {
        tokenChecker.checkBearerToken(headers, userMessage.getName());
        return messageFacade.processMessage(userMessage);
    }
}
