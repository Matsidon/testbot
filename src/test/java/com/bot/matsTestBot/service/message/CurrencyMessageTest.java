package com.bot.matsTestBot.service.message;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyMessageTest {
    private final CurrencyMessage currencyMessage = new CurrencyMessage();

    @Test
    void isSupported() {
        boolean isSupported = currencyMessage.isSupported("/set_currency");
        boolean isNotSupported = currencyMessage.isSupported("/start");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void send() {
        SendMessage sendMessage = currencyMessage.send(1L, "M");

        assertEquals("1", sendMessage.getChatId());
        assertEquals("Пожалуйста, выберите валюту", sendMessage.getText());
        assertNotNull(sendMessage.getReplyMarkup());
    }
}