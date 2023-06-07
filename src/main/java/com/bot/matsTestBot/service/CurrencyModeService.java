package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

public interface CurrencyModeService {

    static CurrencyModeService getInstance() {
        return new HashMapCurrencyModeService();
    }

    ;

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    Currency setOriginalCurrency(long chatId, Currency currency);

    Currency setTargetCurrency(long chatId, Currency currency);

    Boolean setContinueButton(long chatId);

    Boolean getContinueButton(long chatId);
}