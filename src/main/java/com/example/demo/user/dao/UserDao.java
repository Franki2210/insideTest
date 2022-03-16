package com.example.demo.user.dao;

import com.example.demo.user.dto.UserDto;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<UserDto, Integer> {

    UserDto getByName(String name);

}
