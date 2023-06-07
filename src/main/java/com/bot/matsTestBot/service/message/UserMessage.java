package com.bot.matsTestBot.service.message;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public abstract class UserMessage {
    public abstract boolean isSupported(String command);
    public abstract SendMessage send(Long chatId, String userName);
}
