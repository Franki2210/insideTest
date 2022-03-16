package com.example.demo;

import com.example.demo.message.dto.MessageDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helpers {
    public static List<MessageDto> getRandomMessagesFromUser(int count, Integer userId) {
        List<MessageDto> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();

            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            result.add(new MessageDto(userId, generatedString));
        }
        return result;
    }
}
