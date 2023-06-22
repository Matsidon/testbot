package com.bot.matsTestBot.service.callback;

import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.service.CurrencyModeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Optional;

@Slf4j
@Component
public class OriginalCurrencyCallBack extends UserCallBack {
    public OriginalCurrencyCallBack(CurrencyModeService currencyModeService) {
        super(currencyModeService);
    }

    @Override
    public boolean isSupported(String command) {
        return "ORIGINAL".equals(command);
    }

    @Override
    public Optional<SendMessage> execute(long chatId, String[] currency) {
        Currency currencyName = Currency.valueOf(currency[1]);
        getCurrencyModeService().setOriginalCurrency(chatId, currencyName);
        log.info("Произошло изменение выбора origin {} в чате {}", getCurrencyModeService().getOriginalCurrency(chatId).toString(), chatId);
        log.info(getCurrencyModeService().toString());
        return Optional.empty();
    }
}