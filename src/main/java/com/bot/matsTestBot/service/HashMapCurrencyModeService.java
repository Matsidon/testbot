package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class HashMapCurrencyModeService implements CurrencyModeService {
    Map<Long, Currency> savedOriginalCurrencies = new HashMap<>();
    Map<Long, Currency> savedTargetCurrencies = new HashMap<>();
    Map<Long, Boolean> savedContinueButton = new HashMap<>();

    @Override
    public Currency getOriginalCurrency(long chatId) {
        return savedOriginalCurrencies.get(chatId);
    }

    @Override
    public Currency getTargetCurrency(long chatId) {
        return savedTargetCurrencies.get(chatId);
    }

    @Override
    public Currency setOriginalCurrency(long chatId, Currency currency) {
        return savedOriginalCurrencies.put(chatId, currency);
    }

    @Override
    public Currency setTargetCurrency(long chatId, Currency currency) {
        return savedTargetCurrencies.put(chatId, currency);
    }

    @Override
    public Boolean setContinueButton(long chatId) {
        return savedContinueButton.put(chatId, true);
    }

    @Override
    public Boolean getContinueButton(long chatId) {
        return savedContinueButton.get(chatId);
    }
}
