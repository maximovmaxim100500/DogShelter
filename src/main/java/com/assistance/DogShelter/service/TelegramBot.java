package com.assistance.DogShelter.service;

import com.assistance.DogShelter.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


/**
 * Основной класс Telegram бота для приюта для собак.
 * Обрабатывает команды и взаимодействует с пользователями через Telegram.
 */
@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;

    /**
     * Конструктор класса TelegramBot.
     * @param botConfig Конфигурация бота.
     */
    public TelegramBot(@Autowired BotConfig botConfig) {
        this.botConfig = botConfig;
        // Устанавливаем список команд для бота при его создании
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "начать"));
        listOfCommands.add(new BotCommand("/help", "помощь"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's commands list", e.getMessage());
        }
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
        // Проверяем, получено ли сообщение и содержит ли оно текст
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Получаем текст сообщения и id чата
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Обрабатываем сообщение в зависимости от его содержания
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
        } else if (update.hasCallbackQuery()) { //Проверяем данные, отправленные боту от всплывающих кнопок
            String callbackData = update.getCallbackQuery().getData(); //те самые данные, которые бот получит от кнопки
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "Приют_1" -> {
                    String text = "Приют " + callbackData + ". Выберете действие:";
                    shelterMenu(chatId, messageId, text);
                }
                case "Приют_2" -> {
                    String text = "Приют Pick up the dog. Выберете действие:";
                    shelterMenu(chatId, messageId, text);
                }
                case "ShelterInfo" -> sendMessage(chatId, "Информация о приюте");
                case "TakeTheDog" -> sendMessage(chatId, "Чтобы взять собаку нужно...");
                case "DogReport" -> sendMessage(chatId, "Вот ваш отчет");
                case "CallVolunteer" -> sendMessage(chatId, "Зовем волонтера");
                case "ComeBack1" -> choosingShelter(chatId);
            }
        }
    }

    /**
     * Обрабатывает команду /start.
     * @param chatId Идентификатор чата.
     * @param name Имя пользователя.
     */
    public void startCommandReceived(long chatId, String name) {
        String answer = "Hi, " + name + "! Nice to meet you!";
        log.info("Replied to " + name);
        sendMessage(chatId, answer);
    }

    /**
     * Отправляет сообщение в чат.
     * @param chatId Идентификатор чата.
     * @param textToSend Текст сообщения.
     */
    private void sendMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage(); //специальный класс для отправки сообщений
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);

        creatingKeyboard(sendMessage);//Создаем кнопки клавиатуры

        try {
            execute(sendMessage);
            log.info("Sent message: \"" + textToSend + "\" to chatId: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage(), e);
        }
    }

    /**
     * Создает клавиатуру для отправляемого сообщения.
     * @param sendMessage Сообщение, к которому прикрепляется клавиатура.
     */
    private void creatingKeyboard(SendMessage sendMessage) {//Создаем кнопки клавиатуры
        //Как я понял, кнопки клавиатуры создаются один раз и будут висеть внизу всегда, пока мы их не заменим на другие
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(); // инжектим специальный класс разметки клавиатуры
        List<KeyboardRow> keyboardRows = new ArrayList<>(); // Список рядов наших кнопок.
        KeyboardRow row = new KeyboardRow(); //первый ряд кнопок
        row.add("/start"); //порядок создания имеет значение
        row.add("/help");
        keyboardRows.add(row);
        keyboardMarkup.setKeyboard(keyboardRows); //этот метод устанавливает наш список кнопок в качестве клавиатуры
        sendMessage.setReplyMarkup(keyboardMarkup); //этот метод прикрепляет нашу клавиатуру к сообщению, которое будет
        //отправлено пользователю
    }

    /**
     * Отправляет сообщение пользователю для выбора приюта.
     * @param chatId Идентификатор чата.
     */
    private void choosingShelter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберете приют");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>(); //Список одного ряда кнопок
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();//Список списков наших кнопок

        var buttonStrayDogs = new InlineKeyboardButton(); //создаем кнопку
        buttonStrayDogs.setText("Приют 1"); //текст кнопки
        buttonStrayDogs.setCallbackData("Приют_1"); //данные, которые кнопка отправляет боту при ее нажатии

        var buttonPickUpTheDog = new InlineKeyboardButton();//создаем кнопку
        buttonPickUpTheDog.setText("Приют 2");//текст кнопки
        buttonPickUpTheDog.setCallbackData("Приют_2");//данные, которые кнопка отправляет боту при ее нажатии

        rowInLine.add(buttonStrayDogs); //порядок создания имеет значение
        rowInLine.add(buttonPickUpTheDog);
        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine); //этот метод устанавливает наш список кнопок
        sendMessage.setReplyMarkup(markupInLine);//этот метод прикрепляет нашу клавиатуру к сообщению, которое будет
        //отправлено пользователю

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage(), e);
        }
    }

    /**
     * Отправляет меню для выбранного приюта.
     * @param chatId Идентификатор чата.
     * @param messageId Идентификатор сообщения.
     * @param text Текст сообщения.
     */
    private void shelterMenu(long chatId, long messageId, String text) {
        EditMessageText message = new EditMessageText(); //специальный класс для замены последнего сообщения
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);
        //Создаем кнопки. Эти кнопки появятся под сообщением
        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup(); //инжектим класс всплывающих под сообщением кнопок
        List<InlineKeyboardButton> rowInLine1 = new ArrayList<>(); //Список первого ряда кнопок
        List<InlineKeyboardButton> rowInLine2 = new ArrayList<>(); //Список второго ряда кнопок
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>(); //Список третьего ряда кнопок
        List<InlineKeyboardButton> rowInLine4 = new ArrayList<>(); //Список четвертого ряда кнопок
        List<InlineKeyboardButton> rowInLine5 = new ArrayList<>(); //Список четвертого ряда кнопок
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();//Список списков наших кнопок

        var buttonShelterInfo = new InlineKeyboardButton();
        buttonShelterInfo.setText("Узнать информацию о приюте");
        buttonShelterInfo.setCallbackData("ShelterInfo");

        var buttonTakeTheDog = new InlineKeyboardButton();
        buttonTakeTheDog.setText("Как взять животное из приюта");
        buttonTakeTheDog.setCallbackData("TakeTheDog");

        var buttonDogReport = new InlineKeyboardButton();
        buttonDogReport.setText("Прислать отчет о питомце");
        buttonDogReport.setCallbackData("DogReport");

        var buttonCallVolunteer = new InlineKeyboardButton();
        buttonCallVolunteer.setText("Позвать волонтера");
        buttonCallVolunteer.setCallbackData("CallVolunteer");

        var buttonBack = new InlineKeyboardButton();
        buttonCallVolunteer.setText("Назад");
        buttonCallVolunteer.setCallbackData("ComeBack1");

        rowInLine1.add(buttonShelterInfo);
        rowInLine2.add(buttonTakeTheDog);
        rowInLine3.add(buttonDogReport);
        rowInLine4.add(buttonCallVolunteer);
        rowsInLine.add(rowInLine1);
        rowsInLine.add(rowInLine2);
        rowsInLine.add(rowInLine3);
        rowsInLine.add(rowInLine4);
        rowsInLine.add(rowInLine5);

        markupInLine.setKeyboard(rowsInLine); //этот метод устанавливает наш список кнопок
        message.setReplyMarkup(markupInLine);//этот метод прикрепляет нашу клавиатуру к сообщению, которое будет
        //отправлено пользователю

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred while sending message: " + e.getMessage(), e);
        }
    }
}
