package com.assistance.DogShelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
@Component
@Slf4j
public class CallBackQueryHandler {

    /**
     * Обрабатывает обратный вызов (callback) от пользователя.
     * Определяет действие на основе данных обратного вызова и выполняет соответствующее действие.
     *
     * @param update Обновление от Telegram, содержащее информацию о callback'е.
     * @param bot    Экземпляр TelegramBot, через который отправляются сообщения и управляется ботом.
     */
    public void handleCallbackQuery(Update update, TelegramBot bot) {
        // Получаем данные callback, которые пользователь отправил
        String callbackData = update.getCallbackQuery().getData();
        // Получаем идентификатор сообщения, в котором был callback
        long messageId = update.getCallbackQuery().getMessage().getMessageId();
        // Получаем идентификатор чата, в котором был callback
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        // Обрабатываем разные callback в зависимости от их данных
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
            case "ComeBack1":
                // Возвращаем пользователя к выбору приюта
                bot.choosingShelter(chatId);
                break;
            default:
                // Логируем предупреждение о неизвестных данных callback
                log.warn("Unknown callback data received: " + callbackData);
        }
    }
}