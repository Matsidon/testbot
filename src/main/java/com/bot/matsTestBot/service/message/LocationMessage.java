package com.bot.matsTestBot.service.message;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public class LocationMessage extends UserMessage{
    @Override
    public boolean isSupported(String command) {
        return false;
    }

    @Override
    public SendMessage send(Long chatId, String userName) {
        return null;
    }
}
