package com.example.demo.message.service.impl;

import com.example.demo.message.dao.MessageDao;
import com.example.demo.message.dto.MessageDto;
import com.example.demo.message.service.IMessageService;
import com.example.demo.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MessageService implements IMessageService {

    private final MessageDao messageDao;

    @Autowired
    public MessageService(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public List<String> getLast10MessagesByUser(UserDto user) {
        List<MessageDto> messagesDto = messageDao.findByUserIdOrderByIdDesc(user.getId(), PageRequest.of(0, 10));
        return messagesDto.stream().map(MessageDto::getText).toList();
    }

    @Override
    public List<String> saveMessage(UserDto user, String message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(message);
        messageDto.setUserId(user.getId());
        MessageDto savedMessageDto = messageDao.save(messageDto);
        return Collections.singletonList(savedMessageDto.getText());
    }
}
