package com.bot.matsTestBot.service.callback;

import com.bot.matsTestBot.service.CurrencyModeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Getter
@Setter
public abstract class UserCallBack {
    private final CurrencyModeService currencyModeService;

    protected UserCallBack(CurrencyModeService currencyModeService) {
        this.currencyModeService = currencyModeService;
    }

    public abstract boolean isSupported(String command);
    public abstract SendMessage execute(long chatId, String[] currency);
}
