package com.assistance.DogShelter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработчик текстовых сообщений от пользователя.
 * Определяет тип сообщения и выполняет соответствующее действие.
 */
@Component
@Slf4j
public class TextMessageHandler {

    /**
     * Обрабатывает текстовые сообщения от пользователя.
     * Определяет тип сообщения и выполняет соответствующее действие.
     *
     * @param update Обновление от Telegram, содержащее информацию о сообщении.
     * @param bot    Экземпляр TelegramBot, через который отправляются сообщения и управляется ботом.
     */
    public void handleTextMessage(Update update, TelegramBot bot) {
        // Получаем текст сообщения
        String messageText = update.getMessage().getText();
        // Получаем идентификатор чата
        long chatId = update.getMessage().getChatId();

        // Обрабатываем разные команды в зависимости от текста сообщения
        switch (messageText) {
            case "/start":
                // Обрабатываем команду /start и передаем имя пользователя
                bot.startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
            case "/help":
                // Обрабатываем команду /help и отправляем сообщение с инструкцией
                bot.sendMessage(chatId, "Нажмите /start чтобы начать");
                break;
            default:
                // Отправляем сообщение о том, что команда не распознана
                bot.sendMessage(chatId, "Извините, указанная команда не распознана");
        }
    }
}