package com.bot.matsTestBot.service.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class WrongMessage extends UserMessage {
    @Override
    public boolean isSupported(String command) {
        return false;
    }

    @Override
    public SendMessage send(Long chatId, String userName) {
        String answer = userName + ", Я такое еще не умею";
        return new SendMessage(String.valueOf(chatId), answer);
    }
}
