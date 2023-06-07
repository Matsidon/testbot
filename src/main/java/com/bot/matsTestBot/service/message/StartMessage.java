package com.bot.matsTestBot.service.message;

import com.bot.matsTestBot.service.message.UserMessage;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class StartMessage extends UserMessage {

    @Override
    public boolean isSupported(String command) {
        return "/start".equals(command);
    }

    @Override
    public SendMessage send(Long chatId, String userName) {
        String answer = "Привет, " + userName;
        return new SendMessage(String.valueOf(chatId), answer);
    }
}
