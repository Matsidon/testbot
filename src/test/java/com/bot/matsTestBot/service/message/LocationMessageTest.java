package com.bot.matsTestBot.service.message;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

class LocationMessageTest {
    private final LocationMessage locationMessage = new LocationMessage();

    @Test
    void isSupported() {
        boolean isSupported = locationMessage.isSupported("/location");
        boolean isNotSupported = locationMessage.isSupported("/start");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void send() {
        SendMessage sendMessage = locationMessage.send(1L, "M");

        assertEquals("1", sendMessage.getChatId());
        assertEquals("Отправь свою геолокацию, чтобы я смог подсказать, какая станция метро ближе.",
                sendMessage.getText());
    }
}