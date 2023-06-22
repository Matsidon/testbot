package com.bot.matsTestBot.service.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StartMessageTest {
    private final StartMessage startMessage;

    @Autowired
    StartMessageTest(StartMessage startMessage) {
        this.startMessage = startMessage;
    }

    @Test
    void isSupported() {
        boolean isSupported = startMessage.isSupported("/start");
        boolean isNotSupported = startMessage.isSupported("/end");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void send() {
        SendMessage sendMessage = startMessage.send(1L, "M");

        assertEquals("1", sendMessage.getChatId());
        assertEquals("Привет, M", sendMessage.getText());
    }
}