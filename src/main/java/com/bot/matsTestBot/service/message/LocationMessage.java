package com.bot.matsTestBot.service.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class LocationMessage extends UserMessage {
    @Override
    public boolean isSupported(String command) {
        return "/location".equals(command);
    }

    @Override
    public SendMessage send(Long chatId, String userName) {
        String answer = "Отправь свою геолокацию, чтобы я смог подсказать, какая станция метро ближе.";
        return new SendMessage(String.valueOf(chatId), answer);
    }
}
