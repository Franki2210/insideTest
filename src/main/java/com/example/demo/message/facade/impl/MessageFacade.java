package com.example.demo.message.facade.impl;

import com.example.demo.message.facade.IMessageFacade;
import com.example.demo.message.model.UserMessage;
import com.example.demo.message.service.IMessageService;
import com.example.demo.user.dto.UserDto;
import com.example.demo.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageFacade implements IMessageFacade {

    private final IMessageService messageService;
    private final IUserService userService;

    @Autowired
    public MessageFacade(IMessageService messageService, IUserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public List<String> processMessage(UserMessage userMessage) {
        String message = userMessage.getMessage();
        String username = userMessage.getName();
        UserDto user = userService.getByName(username);

        return message.equals("history 10")
                ? messageService.getLast10MessagesByUser(user)
                : messageService.saveMessage(user, message);
    }

}
