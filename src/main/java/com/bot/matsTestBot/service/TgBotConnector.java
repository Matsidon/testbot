package com.bot.matsTestBot.service;

import com.bot.matsTestBot.service.callback.UserCallBack;
import com.bot.matsTestBot.service.message.UserMessage;
import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.config.TgBotConfig;
import com.bot.matsTestBot.service.message.WrongMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class TgBotConnector extends TelegramLongPollingBot {
    private final TgBotConfig tgBotConfig;
    private final CurrencyModeService currencyModeService;
    private final CurrencyConversionService currencyConversionService;
    private final List<UserMessage> messages;
    private final List<UserCallBack> callBacks;

    public TgBotConnector(TgBotConfig tgBotConfig, CurrencyModeService currencyModeService,
                          CurrencyConversionService currencyConversionService, List<UserMessage> messages, List<UserCallBack> callBacks) {
        this.tgBotConfig = tgBotConfig;
        this.currencyModeService = currencyModeService;
        this.currencyConversionService = currencyConversionService;
        this.messages = messages;
        this.callBacks = callBacks;
    }

    @Override
    public String getBotUsername() {
        return tgBotConfig.getName();
    }

    @Override
    public String getBotToken() {
        return tgBotConfig.getKey();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasPreCheckoutQuery()) {
            log.info("получили заказик id = {}, sum = {}, from = {}, order = {}",
                    update.getPreCheckoutQuery().getId(),
                    update.getPreCheckoutQuery().getTotalAmount(),
                    update.getPreCheckoutQuery().getFrom(),
                    update.getPreCheckoutQuery().getOrderInfo());
            execute(AnswerPreCheckoutQuery
                    .builder()
                    .preCheckoutQueryId(update.getPreCheckoutQuery().getId())
                    .errorMessage("не получилось")
                    .ok(true)
                    .build());
        } else if (update.hasMessage() && update.getMessage().hasLocation()) {
            Message message = update.getMessage();
            handleLocation(message);
        } else if (update.hasCallbackQuery()) {
            handleCallBack(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            handleMessage(message);
        }

    }

    public void handleLocation(Message message) {
        //todo
        long chatId = message.getChatId();
        String userName = message.getChat().getFirstName();
        double longitude = message.getLocation().getLongitude();
        double latitude = message.getLocation().getLatitude();

    }

    @SneakyThrows
    public void handleMessage(Message message) {
        long chatId = message.getChatId();
        String userName = message.getChat().getFirstName();
        Optional<MessageEntity> commandEntity = message.getEntities() != null ?
                message.getEntities()
                        .stream()
                        .filter(e -> "bot_command".equals(e.getType()))
                        .findFirst() : Optional.empty();
        if (commandEntity.isPresent()) {
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getOffset() + commandEntity.get().getLength());

            SendMessage sendMessage = messages.stream()
                    .filter(m -> m.isSupported(command))
                    .findFirst()
                    .map(m -> m.send(chatId, userName))
                    .orElse(new WrongMessage().send(chatId, userName));
            execute(sendMessage);
        }
        try {
            String count = message.getText();
            double res = currencyConversionService.convert(currencyModeService.getOriginalCurrency(chatId), currencyModeService.getTargetCurrency(chatId), Double.parseDouble(count));
            log.info(String.valueOf(res));
            sendMessage(chatId, count + " " + currencyModeService.getOriginalCurrency(chatId).name() + " = " + String.format("%.2f", res) + " " + currencyModeService.getTargetCurrency(chatId).name());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void handleCallBack(Update update) {
        String[] currency = update.getCallbackQuery().getData().split(": ");
        Message message = update.getCallbackQuery().getMessage();
        long chatId = message.getChatId();
        String userName = message.getChat().getFirstName();
        String action = currency[0];
        Optional<SendMessage> sendMessage = callBacks.stream()
                .filter(c -> c.isSupported(action))
                .findFirst()
                .map(c -> c.execute(chatId, currency))
                .orElse(Optional.ofNullable(new WrongMessage().send(chatId, userName)));

        if (sendMessage.isPresent()) {
            execute(sendMessage.get());
        }

        List<List<InlineKeyboardButton>> currencyList = getCurrencies(currencyModeService.getOriginalCurrency(chatId), currencyModeService.getTargetCurrency(chatId));
        currencyList = checkContinueButton(chatId, currencyList);
        execute(EditMessageReplyMarkup.builder().chatId(String.valueOf(chatId))
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(currencyList).build())
                .build());
        log.info(currencyModeService.toString());
    }

    private List<List<InlineKeyboardButton>> checkContinueButton(long chatId, List<List<InlineKeyboardButton>>
            currencyList) {
        if (isCanToContinue(chatId)) {
            if (currencyModeService.getContinueButton(chatId) != null && currencyModeService.getContinueButton(chatId).booleanValue()) {
                currencyList.add(List.of(InlineKeyboardButton.builder().text("Продолжить\uD83D\uDD3D").callbackData("выбраны валюты").build()));
            } else {
                currencyList.add(List.of(InlineKeyboardButton.builder().text("Продолжить").callbackData("выбраны валюты").build()));
            }
        }
        return currencyList;
    }

    private boolean isCanToContinue(long chatId) {
        return currencyModeService.getOriginalCurrency(chatId) != null && currencyModeService.getTargetCurrency(chatId) != null;
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

    private void sendMessage(long chatId, String answer) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrencyButton(Currency savedCurrency, Currency currentCurrency) {
        if (savedCurrency == currentCurrency) {
            return currentCurrency.name() + "✅";
        }
        return currentCurrency.name();
    }

    private String getContinueButton() {
        return "Продолжить \uD83D\uDD3D ✅";
    }
}
