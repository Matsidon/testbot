package com.bot.matsTestBot.service.callback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyCallBackTest {
    private final CurrencyCallBack currencyCallBack;

    @Autowired
    CurrencyCallBackTest(CurrencyCallBack currencyCallBack) {
        this.currencyCallBack = currencyCallBack;
    }

    @Test
    void isSupported() {
        boolean isSupported = currencyCallBack.isSupported("выбраны валюты");
        boolean isNotSupported = currencyCallBack.isSupported("start");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void execute() {
        Optional<SendMessage> sendMessage = currencyCallBack.execute(1L, new String[]{"выбраны валюты",""});

        assertEquals("Введите сумму в null", sendMessage.get().getText());
    }
}