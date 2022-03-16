package com.example.demo.user.dao;

import com.example.demo.user.dao.UserDao;
import com.example.demo.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void userDaoTest() {
        UserDto user1 = new UserDto("testUser1", "password1");
        UserDto user2 = new UserDto("testUser2", "password2");
        userDao.save(user1);
        userDao.save(user2);

        Assertions.assertEquals(userDao.getByName("testUser1"), user1);
        Assertions.assertEquals(userDao.getByName("testUser2"), user2);
        Assertions.assertNull(userDao.getByName(""));
    }
}
