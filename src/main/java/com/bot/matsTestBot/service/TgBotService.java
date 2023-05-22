package com.bot.matsTestBot.service;

import com.bot.matsTestBot.model.Currency;
import com.bot.matsTestBot.config.TgBotConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TgBotService extends TelegramLongPollingBot {
    private final TgBotConfig tgBotConfig;
    private final CurrencyModeService currencyModeService = CurrencyModeService.getInstance();
    private final CurrencyConversionService currencyConversionService = CurrencyConversionService.getInstance();

    @Autowired
    public TgBotService(TgBotConfig tgBotConfig) {
        this.tgBotConfig = tgBotConfig;
    }

    @Override
    public String getBotUsername() {
        return tgBotConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return tgBotConfig.getToken();
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
        } else if (update.hasCallbackQuery()) {
            handleCallBack(update);
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            handleMessage(message);
        }
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
            switch (command) {
                case "/start":
                    startCommandReceived(chatId, userName);
                    break;
                case "/set_currency":
                    Currency original = currencyModeService.getOriginalCurrency(message.getChatId());
                    Currency target = currencyModeService.getTargetCurrency(message.getChatId());
                    log.info("новый origin {} и новый target {}", original, target);
                    sendMessageWithBotton(chatId, "Пожалуйста, выберите валюту", getCurrencies(original, target));
                    break;
                case "/buy":
                    buySomething(chatId);
                    break;
                default:
                    sendMessage(chatId, "Я такое еще не умею");
                    break;
            }
        } else if (message.getText().toLowerCase().equals("я ксюша") || message.getText().toLowerCase().equals("это ксюша")) {
            sendMessage(chatId, " ❤️");
        } else {
            try {
                String count = message.getText();
                double res = currencyConversionService.convert(currencyModeService.getOriginalCurrency(chatId), currencyModeService.getTargetCurrency(chatId), Double.parseDouble(count));
                log.info(String.valueOf(res));
                sendMessage(chatId, count + " " + currencyModeService.getOriginalCurrency(chatId).name() + " = " + String.format("%.2f", res) + " " + currencyModeService.getTargetCurrency(chatId).name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buySomething(long chatId) throws TelegramApiException {
        execute(SendInvoice.builder()
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

    @SneakyThrows
    private void handleCallBack(Update update) {
        String[] currency = update.getCallbackQuery().getData().split(": ");
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String action = currency[0];
        Currency currencyName;
        switch (action) {
            case "ORIGINAL":
                currencyName = Currency.valueOf(currency[1]);
                currencyModeService.setOriginalCurrency(chatId, currencyName);
                log.info("Произошло изменение выбора origin {} в чате {}", currencyModeService.getOriginalCurrency(chatId).toString(), chatId);
                log.info(currencyModeService.toString());
                break;
            case "TARGET":
                currencyName = Currency.valueOf(currency[1]);
                currencyModeService.setTargetCurrency(chatId, currencyName);
                log.info("Произошло изменение выбора target {} в чате {}" + currencyModeService.getTargetCurrency(chatId).toString(), chatId);
                log.info(currencyModeService.toString());
                break;
            case "выбраны валюты":
                currencyModeService.setContinueButton(chatId);
                sendMessage(chatId, String.format("Введите сумму в %s", currencyModeService.getOriginalCurrency(chatId).name()));
                break;
        }
        List<List<InlineKeyboardButton>> currencyList = getCurrencies(currencyModeService.getOriginalCurrency(chatId), currencyModeService.getTargetCurrency(chatId));
        currencyList = checkContinueButton(chatId, currencyList);
        execute(EditMessageReplyMarkup.builder().chatId(String.valueOf(chatId)).messageId(update.getCallbackQuery().getMessage().getMessageId()).replyMarkup(InlineKeyboardMarkup.builder().keyboard(currencyList).build()).build());
        log.info(currencyModeService.toString());
    }

    private List<List<InlineKeyboardButton>> checkContinueButton(long chatId, List<List<InlineKeyboardButton>> currencyList) {
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

    private void startCommandReceived(long chatId, String userName) {
        String answer = "Привет, " + userName;
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String answer) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
            execute(sendMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageWithBotton(long chatId, String answer, List<List<InlineKeyboardButton>> currencyList) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(chatId), answer);
            sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().keyboard(currencyList).build());
            log.info("Отправили сообщение с валютами");
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
