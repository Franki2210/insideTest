package com.example.demo.message.dao;

import com.example.demo.Helpers;
import com.example.demo.message.dto.MessageDto;
import com.example.demo.user.dao.UserDao;
import com.example.demo.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MessageDaoTest {

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Test
    public void messageDaoTest() {
        UserDto user1 = new UserDto("testUser1", "password1");
        UserDto user2 = new UserDto("testUser2", "password2");;
        userDao.save(user1);
        userDao.save(user2);

        List<MessageDto> messagesForUser1 = Helpers.getRandomMessagesFromUser(12, user1.getId());
        List<MessageDto> messagesForUser2 = Helpers.getRandomMessagesFromUser(5, user2.getId());

        messageDao.saveAll(messagesForUser1);
        messageDao.saveAll(messagesForUser2);

        Assertions.assertEquals(
                messageDao.findByUserIdOrderByIdDesc(user1.getId(), PageRequest.of(0, 10)),
                getLast10Entities(messagesForUser1));
        Assertions.assertEquals(
                messageDao.findByUserIdOrderByIdDesc(user2.getId(), PageRequest.of(0, 10)),
                getLast10Entities(messagesForUser2));
        Assertions.assertEquals(
                messageDao.findByUserIdOrderByIdDesc(null, PageRequest.of(0, 10)),
                new ArrayList<MessageDto>());
    }

    private <T> List<T> getLast10Entities(List<T> list) {
        int fromIndex = list.size() - 10;
        fromIndex = Math.max(0, fromIndex);
        int toIndex = list.size();
        List<T> lastEntities = list.subList(fromIndex, toIndex);
        Collections.reverse(lastEntities);
        return lastEntities;
    }
}
