package com.assistance.DogShelter.service;

import com.assistance.DogShelter.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    public TelegramBot(@Autowired BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    choosingShelter(chatId);
                    break;
                case "/help":
                    sendMessage(chatId, "Нажмите /start чтобы начать");
                    break;
                default:
                    sendMessage(chatId, "Sorry, required command is not recognized");
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.equals("Stray_dogs")) {
                String text = "Приют Stray dogs. Выберете действие:";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int) messageId);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error("Error occurred while sending message: " + e.getMessage(), e);
                }
            } else if (callbackData.equals("Pick_up_the_dog")) {
                String text = "Приют Pick up the dog. Выберете действие:";
                EditMessageText message = new EditMessageText();
                message.setChatId(String.valueOf(chatId));
                message.setText(text);
                message.setMessageId((int) messageId);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    log.error("Error occurred while sending message: " + e.getMessage(), e);
                }
            }
        }
    }

    public void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + "! Nice to meet you!";
        log.info("Replied to " + name);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        //Создаем кнопку клавиатуры
        //По сути эта кнопка появляется в ответ на сообщение и т.к. она в методе который отправляет сообщение,
        //то кнопка будет внизу всегда. Если этот кусок кода перенести в onUpdateReceived в case "/start":, то
        //кнопка будет появляться только после команды /start.
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(); // инжектим специальный класс разметки клавиатуры
        List<KeyboardRow> keyboardRows = new ArrayList<>(); // Список рядов наших кнопок.
        KeyboardRow row = new KeyboardRow(); //первый ряд кнопок
        row.add("/start"); //порядок создания имеет значение
        row.add("/help");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows); //этот метод устанавливает наш список кнопок в качестве клавиатуры
        sendMessage.setReplyMarkup(keyboardMarkup); //этот метод прикрепляет нашу клавиатуру к сообщению, которое будет
        //отправлено пользователю

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage(), e);
        }
    }

    private void choosingShelter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Choose a shelter");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>(); //Список одного ряда кнопок
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();//Список списков наших кнопок

        var buttonStrayDogs = new InlineKeyboardButton();
        buttonStrayDogs.setText("Stray dogs");
        buttonStrayDogs.setCallbackData("Stray_dogs");

        var buttonPickUpTheDog = new InlineKeyboardButton();
        buttonPickUpTheDog.setText("Pick up the dog");
        buttonPickUpTheDog.setCallbackData("Pick_up_the_dog");

        rowInLine.add(buttonStrayDogs); //порядок создания имеет значение
        rowInLine.add(buttonPickUpTheDog);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        sendMessage.setReplyMarkup(markupInLine);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage(), e);
        }
    }
}
