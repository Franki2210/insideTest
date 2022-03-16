package com.example.demo.user.service;

import com.example.demo.user.dto.UserDto;

public interface IUserService {
    UserDto getByName(String name);
}
