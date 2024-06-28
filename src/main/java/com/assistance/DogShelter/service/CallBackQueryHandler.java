package com.assistance.DogShelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class CallBackQueryHandler {
    private final VolunteerRegistrationService volunteerRegistrationService;
    private final TextMessageHandler textMessageHandler;
    private final ApplicationContext applicationContext;

    @Autowired
    public CallBackQueryHandler(VolunteerRegistrationService volunteerRegistrationService, TextMessageHandler textMessageHandler, ApplicationContext applicationContext) {
        this.volunteerRegistrationService = volunteerRegistrationService;
        this.textMessageHandler = textMessageHandler;
        this.applicationContext = applicationContext;
    }

    public void handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        try {
            switch (callbackData) {
                case "Приют_1":
                    String text1 = "Приют1. Выберите действие:";
                    bot.showShelterInfo(chatId, messageId, text1);
                    break;
                case "Приют_2":
                    String text2 = "Приют2. Выберите действие:";
                    bot.showShelterInfo(chatId, messageId, text2);
                    break;
                case "ShelterInfo":
                    bot.sendMessage(chatId, "Информация о приюте");
                    break;
                case "TakeTheDog":
                    bot.sendMessage(chatId, "Чтобы взять собаку нужно...");
                    break;
                case "DogReport":
                    bot.sendMessage(chatId, "Вот ваш отчет");
                    break;
                case "CallVolunteer":
                    bot.sendMessage(chatId, "Зовем волонтера");
                    break;
                case "RegisterVolunteer":
                    textMessageHandler.handleTextMessage(update);
                    break;
                case "ComeBack1":
                    bot.choosingShelter(chatId);
                    break;
                default:
                    log.warn("Unknown callback data received: " + callbackData);
            }
        } catch (Exception e) {
            log.error("Error while handling callback query: " + callbackData, e);
            bot.sendMessage(chatId, "Произошла ошибка при обработке вашего запроса. Пожалуйста, попробуйте позже.");
        }
    }
}
