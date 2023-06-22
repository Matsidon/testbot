package com.bot.matsTestBot.service.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WrongMessageTest {
    private final WrongMessage wrongMessage;

    @Autowired
    WrongMessageTest(WrongMessage wrongMessage) {
        this.wrongMessage = wrongMessage;
    }

    @Test
    void isSupported() {
        boolean isNotSupported = wrongMessage.isSupported("/start");

        assertFalse(isNotSupported);
    }

    @Test
    void send() {
        SendMessage sendMessage = wrongMessage.send(1L, "M");

        assertEquals("1", sendMessage.getChatId());
        assertEquals("M, Я такое еще не умею", sendMessage.getText());
    }
}