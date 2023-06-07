package com.bot.matsTestBot.service;

import com.bot.matsTestBot.config.TgBotConfig;
import com.bot.matsTestBot.service.callback.UserCallBack;
import com.bot.matsTestBot.service.message.UserMessage;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class TgBotConnectorTest {
    @InjectMocks
    private final TgBotConnector tgBotConnector;
    @Mock
    private final TgBotConfig tgBotConfig;
    @Mock
    private final CurrencyModeService currencyModeService;
    @Mock
    private final CurrencyConversionService currencyConversionService;
    @Mock
    private final List<UserMessage> messages;
    @Mock
    private final List<UserCallBack> callBacks;

    @Autowired
    public TgBotConnectorTest(TgBotConnector tgBotConnector, TgBotConfig tgBotConfig, CurrencyModeService currencyModeService,
                              CurrencyConversionService currencyConversionService, List<UserMessage> messages, List<UserCallBack> callBacks) {
        this.tgBotConnector = tgBotConnector;
        this.tgBotConfig = tgBotConfig;
        this.currencyModeService = currencyModeService;
        this.currencyConversionService = currencyConversionService;
        this.messages = messages;
        this.callBacks = callBacks;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getBotUsername() {
    }

    @Test
    void getBotToken() {
    }

    @Test
    void onUpdateReceived() {
    }

    @Test
    void handleStartMessage() {

        User user = new User(877034740L, "M", false);
        Chat chat = new Chat(877034740L, "private");
        Message message = new Message();
        message.setChat(chat);
        message.setMessageId(481);
        message.setDate(123456);
        message.setFrom(user);
        message.setText("вот такое сообщение");
        tgBotConnector.handleMessage(message);
    }
}