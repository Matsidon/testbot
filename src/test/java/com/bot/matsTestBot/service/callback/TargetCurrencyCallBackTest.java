package com.bot.matsTestBot.service.callback;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TargetCurrencyCallBackTest {
    private final TargetCurrencyCallBack targetCurrencyCallBack;

    @Autowired
    TargetCurrencyCallBackTest(TargetCurrencyCallBack targetCurrencyCallBack) {
        this.targetCurrencyCallBack = targetCurrencyCallBack;
    }

    @Test
    void isSupported() {
        boolean isSupported = targetCurrencyCallBack.isSupported("TARGET");
        boolean isNotSupported = targetCurrencyCallBack.isSupported("/start");

        assertTrue(isSupported);
        assertFalse(isNotSupported);
    }

    @Test
    void execute() {
        Optional<SendMessage> sendMessage = targetCurrencyCallBack.execute(1L, new String[]{"TARGET", "RUB"});

        assertEquals(Optional.empty(), sendMessage);
    }
}