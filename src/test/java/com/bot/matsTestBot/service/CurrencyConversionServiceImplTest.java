package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class CurrencyConversionServiceImplTest {
    private final CurrencyConversionServiceImpl currencyConversionService;

    @Autowired
    CurrencyConversionServiceImplTest(CurrencyConversionServiceImpl currencyConversionService) {
        this.currencyConversionService = currencyConversionService;
    }

    @Test
    void convert() {
        double res = currencyConversionService.convert(Currency.RUB, Currency.EUR, 30);
        assertNotEquals(-1, res);
    }
}