package com.bot.matsTestBot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class BuyMessage {
private AbsSender absSender;

    public boolean isSupported(String command) {
        return "/buy".equals(command);
    }


    @SneakyThrows
    public void send(Long chatId, String userName) {
        absSender.execute(SendInvoice.builder()
                .startParameter("w")
                .chatId(String.valueOf(chatId))
                .currency("RUB")
                .providerToken("401643678:TEST:bb4b4bbc-8255-462d-afec-829437bdbe5f")
                .title("Консультация")
                .description("оплата психологической консультации")
                .payload("tg_payment")
                .price(new LabeledPrice("руб", 10000))
                .photoUrl("https://www.meme-arsenal.com/memes/e84b89b89a696c8ae6cd7ffee2aa5f80.jpg")
                .protectContent(true)
                .sendPhoneNumberToProvider(true)
                .needPhoneNumber(true)
                .sendEmailToProvider(true)
                .needEmail(true)
                .providerData("")
                .maxTipAmount(100000)
                .suggestedTipAmount(1000)
                .suggestedTipAmount(2000)
                .suggestedTipAmount(3000)
                .suggestedTipAmount(5000)
                .build());
    }
}
