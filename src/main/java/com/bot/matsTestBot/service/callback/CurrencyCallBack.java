package com.bot.matsTestBot.service.callback;

import com.bot.matsTestBot.service.CurrencyModeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Getter
@Setter
public class CurrencyCallBack extends UserCallBack {

    protected CurrencyCallBack(CurrencyModeService currencyModeService) {
        super(currencyModeService);
    }

    @Override
    public boolean isSupported(String command) {
        return "выбраны валюты".equals(command);
    }

    @Override
    public SendMessage execute(long chatId, String[] currency) {
        getCurrencyModeService().setContinueButton(chatId);
        return new SendMessage(String.valueOf(chatId), String.format("Введите сумму в %s", getCurrencyModeService().getOriginalCurrency(chatId).name()));
    }
}
