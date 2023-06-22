package com.bot.matsTestBot.service;

import com.bot.matsTestBot.service.message.UserMessage;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TgBotConnectorTest {
    @InjectMocks
    private final TgBotConnector tgBotConnector;

    @Autowired
    public TgBotConnectorTest(TgBotConnector tgBotConnector) {
        this.tgBotConnector = tgBotConnector;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getBotUsername() {
        String name = tgBotConnector.getBotUsername();
        assertEquals("TestDemoBot", name);
    }

    @Test
    void getBotToken() {
        String token = tgBotConnector.getBotToken();
        assertEquals("6262141686:AAFF4jLjBN_mvOL_XfkmzCg83lopDEvyPi8", token);
    }

    @Test
    void handleStartMessage() {
        Message message = new Message();
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType("bot_command");
        message.setEntities(List.of(messageEntity));
        User user = new User(877034740L, "M", false);
        user.setUserName("M");
        Chat chat = new Chat(877034740L, "private");
        message.setChat(chat);
        message.setMessageId(481);
        message.setDate(123456);
        message.setFrom(user);
        message.setText("/start");

        UserMessage userMessage = Mockito.mock(UserMessage.class);

        when(userMessage.isSupported(any())).thenReturn(true);
        when(userMessage.send(any(), any())).thenReturn(new SendMessage(String.valueOf(1L), "Привет, M"));
        //todo: падает с налпоинтером
        tgBotConnector.handleMessage(message);

        verify(userMessage, times(1)).send(1L, "M");
    }
}