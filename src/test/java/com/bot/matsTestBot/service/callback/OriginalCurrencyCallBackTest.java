package com.bot.matsTestBot.service.callback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class OriginalCurrencyCallBackTest {
    private final OriginalCurrencyCallBack originalCurrencyCallBack;

    @Autowired
    OriginalCurrencyCallBackTest(OriginalCurrencyCallBack originalCurrencyCallBack) {
        this.originalCurrencyCallBack = originalCurrencyCallBack;
    }

    @Test
    void isSupported() {
        boolean isSupported = originalCurrencyCallBack.isSupported("ORIGINAL");
        boolean isNotSupported = originalCurrencyCallBack.isSupported("start");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void execute() {
        Optional<SendMessage> sendMessage = originalCurrencyCallBack.execute(1L, new String[]{"ORIGINAL","RUB"});

        assertEquals(Optional.empty(), sendMessage);
    }
}