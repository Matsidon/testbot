package com.bot.matsTestBot.service.callback;

import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.service.CurrencyModeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

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
    public Optional<SendMessage> execute(long chatId, String[] currency) {
        getCurrencyModeService().setContinueButton(chatId);
        Currency originalCurrency = getCurrencyModeService().getOriginalCurrency(chatId);
        return Optional.of(new SendMessage(String.valueOf(chatId),
                String.format("Введите сумму в %s", originalCurrency != null ?
                        originalCurrency.name() : null)));
    }
}
