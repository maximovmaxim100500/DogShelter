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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CallBackQueryHandler callBackQueryHandler;
    private final TextMessageHandler textMessageHandler;

    /**
     * Конструктор для TelegramBot.
     *
     * @param botConfig           Конфигурация бота.
     * @param callBackQueryHandler Обработчик обратных вызовов.
     * @param textMessageHandler  Обработчик текстовых сообщений.
     */
    @Autowired
    public TelegramBot(BotConfig botConfig, CallBackQueryHandler callBackQueryHandler, TextMessageHandler textMessageHandler) {
        this.botConfig = botConfig;
        this.callBackQueryHandler = callBackQueryHandler;
        this.textMessageHandler = textMessageHandler;

        // Инициализация списка команд для бота
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "начать"));
        listOfCommands.add(new BotCommand("/help", "помощь"));
        try {
            // Установка команд для бота
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка при установке списка команд бота", e);
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

    /**
     * Обработка полученного обновления.
     *
     * @param update Объект обновления.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Обработка текстового сообщения
            textMessageHandler.handleTextMessage(update, this);
        } else if (update.hasCallbackQuery()) {
            // Обработка обратного вызова
            callBackQueryHandler.handleCallbackQuery(update, this);
        }
    }

    /**
     * Отправка текстового сообщения пользователю.
     *
     * @param chatId     Идентификатор чата.
     * @param textToSend Текст для отправки.
     */
    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            // Выполнение отправки сообщения
            execute(message);
            log.info("Отправлено сообщение: \"" + textToSend + "\" в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: " + e.getMessage(), e);
        }
    }

    /**
     * Показ информации о приюте.
     *
     * @param chatId    Идентификатор чата.
     * @param messageId Идентификатор сообщения.
     * @param text      Текст для отображения.
     */
    public void showShelterInfo(long chatId, long messageId, String text) {
        // Создаем объект сообщения для редактирования текста
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        // Создаем объект для разметки инлайн-клавиатуры
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();

        // Создание кнопок для меню информации о приюте
        InlineKeyboardButton buttonOurPets = new InlineKeyboardButton();
        buttonOurPets.setText("Наши питомцы");
        buttonOurPets.setCallbackData("OurPets");

        InlineKeyboardButton buttonSchedule = new InlineKeyboardButton();
        buttonSchedule.setText("Расписание");
        buttonSchedule.setCallbackData("Schedule");

        InlineKeyboardButton buttonDirections = new InlineKeyboardButton();
        buttonDirections.setText("Схема проезда");
        buttonDirections.setCallbackData("Directions");

        InlineKeyboardButton buttonContacts = new InlineKeyboardButton();
        buttonContacts.setText("Контакты");
        buttonContacts.setCallbackData("Contacts");

        InlineKeyboardButton buttonQuestions = new InlineKeyboardButton();
        buttonQuestions.setText("Остались вопросы");
        buttonQuestions.setCallbackData("Questions");

        // Кнопка "Назад" для возврата в меню выбора приютов
        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
        buttonBack.setText("Назад");
        buttonBack.setCallbackData("ComeBack1");

        // Добавление кнопок в строки
        rowInline1.add(buttonOurPets);
        rowInline1.add(buttonSchedule);
        rowInline2.add(buttonDirections);
        rowInline2.add(buttonContacts);
        rowInline2.add(buttonQuestions);
        rowInLine3.add(buttonBack);

        // Добавление строк в список строк
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInLine3);

        // Установка разметки клавиатуры
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            // Выполнение отправки сообщения с кнопками
            execute(message);
            log.info("Отправлено меню информации о приюте в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню информации о приюте: " + e.getMessage(), e);
        }
    }

    /**
     * Обработка команды /start.
     *
     * @param chatId Идентификатор чата.
     * @param name   Имя пользователя.
     */
    public void startCommandReceived(long chatId, String name) {
        // Формирование приветственного сообщения
        String greetingMessage = "Привет, " + name + "! Добро пожаловать в наш приют для собак!";
        sendMessage(chatId, greetingMessage);
        choosingShelter(chatId);
    }

    /**
     * Отображение меню выбора приюта.
     *
     * @param chatId Идентификатор чата.
     */
    protected void choosingShelter(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Выберите приют:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Создание кнопок для выбора приюта
        InlineKeyboardButton buttonShelter1 = new InlineKeyboardButton();
        buttonShelter1.setText("Приют 1");
        buttonShelter1.setCallbackData("Приют_1");

        InlineKeyboardButton buttonShelter2 = new InlineKeyboardButton();
        buttonShelter2.setText("Приют 2");
        buttonShelter2.setCallbackData("Приют_2");

        // Добавление кнопок в строку
        rowInline.add(buttonShelter1);
        rowInline.add(buttonShelter2);
        rowsInline.add(rowInline);

        // Установка разметки клавиатуры
        markupInline.setKeyboard(rowsInline);
        sendMessage.setReplyMarkup(markupInline);

        try {
            // Выполнение отправки сообщения с кнопками
            execute(sendMessage);
            log.info("Отправлено меню выбора приюта в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню выбора приюта: " + e.getMessage(), e);
        }
    }
}
