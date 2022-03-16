package com.example.demo.message.facade;

import com.example.demo.message.model.UserMessage;

import java.util.List;

public interface IMessageFacade {
    List<String> processMessage(UserMessage userMessage);
}
