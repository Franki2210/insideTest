package com.example.demo.message.service;

import com.example.demo.user.dto.UserDto;

import java.util.List;

public interface IMessageService {
    List<String> getLast10MessagesByUser(UserDto user);

    List<String> saveMessage(UserDto user, String message);
}
