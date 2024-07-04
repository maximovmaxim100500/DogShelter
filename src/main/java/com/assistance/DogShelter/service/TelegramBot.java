package com.assistance.DogShelter.service;

import com.assistance.DogShelter.config.BotConfig;
import com.assistance.DogShelter.model.Pet;
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

import java.util.*;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final CallBackQueryHandler callBackQueryHandler;
    private final TextMessageHandler textMessageHandler;
    private final PetService petService;

    @Autowired
    public TelegramBot(BotConfig botConfig, CallBackQueryHandler callBackQueryHandler, TextMessageHandler textMessageHandler, PetService petService) {
        this.botConfig = botConfig;
        this.callBackQueryHandler = callBackQueryHandler;
        this.textMessageHandler = textMessageHandler;
        this.petService = petService;

        // Инициализация списка команд для бота
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Начать"));
        listOfCommands.add(new BotCommand("/register_volunteer", "Регистрация волонтера"));
        listOfCommands.add(new BotCommand("/help", "Помощь"));

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

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            textMessageHandler.handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            callBackQueryHandler.handleCallbackQuery(update);
        }
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Отправлено сообщение: \"" + textToSend + "\" в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: " + e.getMessage(), e);
        }
    }

    public void showShelterInfo(long chatId, long messageId, String text, long shelterId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine3 = new ArrayList<>();

        InlineKeyboardButton buttonOurPets = new InlineKeyboardButton();
        buttonOurPets.setText("Наши питомцы");
        buttonOurPets.setCallbackData("OurPets_" + shelterId);

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

        InlineKeyboardButton buttonBack = new InlineKeyboardButton();
        buttonBack.setText("Назад");
        buttonBack.setCallbackData("ComeBack1");

        rowInline1.add(buttonOurPets);
        rowInline1.add(buttonSchedule);
        rowInline2.add(buttonDirections);
        rowInline2.add(buttonContacts);
        rowInline2.add(buttonQuestions);
        rowInLine3.add(buttonBack);

        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInLine3);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
            log.info("Отправлено меню информации о приюте в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке меню информации о приюте: " + e.getMessage(), e);
        }
    }
    public void showPets(long chatId, long shelterId) {
        // Логика получения питомцев из базы данных по shelterId и отправка сообщений в Telegram
        List<Pet> pets = (List<Pet>) petService.getPetsByShelterId(shelterId);
        StringBuilder petsInfo = new StringBuilder("Наши питомцы:\n");

        for (Pet pet : pets) {
            petsInfo.append("Имя: ").append(pet.getName()).append("\n")
                    .append("Порода: ").append(pet.getBreed()).append("\n")
                    .append("Возраст: ").append(pet.getAge()).append("\n\n");
        }

        sendMessage(chatId, petsInfo.toString());
    }


    public void startCommandReceived(long chatId, String name) {
        // Формирование приветственного сообщения
        String greetingMessage = "Привет, " + name + "! Добро пожаловать в наш приют для собак!";
        sendMessage(chatId, greetingMessage);
        choosingShelter(chatId);
    }

    public void choosingShelter(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите приют:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton buttonShelter1 = new InlineKeyboardButton();
        buttonShelter1.setText("Приют 1");
        buttonShelter1.setCallbackData("Приют_1");

        InlineKeyboardButton buttonShelter2 = new InlineKeyboardButton();
        buttonShelter2.setText("Приют 2");
        buttonShelter2.setCallbackData("Приют_2");

        rowInline.add(buttonShelter1);
        rowInline.add(buttonShelter2);

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        try {
            execute(message);
            log.info("Отправлено сообщение с выбором приюта в чат: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения с выбором приюта: " + e.getMessage(), e);
        }
    }
}
