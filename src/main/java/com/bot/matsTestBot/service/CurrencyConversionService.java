package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;


interface CurrencyConversionService {
    static CurrencyConversionService getInstance(){return new CurrencyConversionServiceImpl();}
    double convert(Currency original, Currency target, double count);
}
