package com.bot.matsTestBot.service.message;

import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.service.CurrencyModeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CurrencyMessage extends UserMessage {
    private final CurrencyModeService currencyModeService = CurrencyModeService.getInstance();

    @Override
    public boolean isSupported(String command) {
        return "/set_currency".equals(command);
    }

    @Override
    public SendMessage send(Long chatId, String userName) {
        Currency original = currencyModeService.getOriginalCurrency(chatId);
        Currency target = currencyModeService.getTargetCurrency(chatId);
        log.info("новый origin {} и новый target {}", original, target);
        return sendMessageWithBotton(chatId, "Пожалуйста, выберите валюту", getCurrencies(original, target));
    }

    private List<List<InlineKeyboardButton>> getCurrencies(Currency original, Currency target) {
        List<List<InlineKeyboardButton>> currencyList = new ArrayList<>();
        for (Currency currency1 : Currency.values()) {
            currencyList.add(List.of(
                    InlineKeyboardButton.builder().text(getCurrencyButton(original, currency1)).callbackData("ORIGINAL: " + currency1.name()).build(),
                    InlineKeyboardButton.builder().text(getCurrencyButton(target, currency1)).callbackData("TARGET: " + currency1.name()).build()
            ));
        }
        return currencyList;
    }

    private String getCurrencyButton(Currency savedCurrency, Currency currentCurrency) {
        if (savedCurrency == currentCurrency) {
            return currentCurrency.name() + "✅";
        }
        return currentCurrency.name();
    }

    private SendMessage sendMessageWithBotton(long chatId, String answer, List<List<InlineKeyboardButton>> currencyList) {
        SendMessage sendMessage = null;
        try {
            sendMessage = new SendMessage(String.valueOf(chatId), answer);
            sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(currencyList).build());
            log.info("Отправили сообщение с валютами");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendMessage;
    }
}
