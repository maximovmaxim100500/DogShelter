package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.PhotoReport;
import com.assistance.DogShelter.db.model.Report;
import com.assistance.DogShelter.db.repository.PhotoReportRepository;
import com.assistance.DogShelter.db.repository.ReportRepository;
import com.assistance.DogShelter.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ReportSendFormService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private PhotoReportRepository photoReportRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Long, Boolean> awaitingText = new HashMap<>();
    private Map<Long, Boolean> awaitingPhoto = new HashMap<>();

    private Report report;
    private PhotoReport photoReport;
    private boolean isTextCreate = false;

    public void sendReportForm(long chatId) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        if (userRepository.findByChatId(chatId).isPresent()) {
            bot.sendMessage(chatId, "Пожалуйста, напишите краткий отчёт о содержании питомца:");
            askForText(chatId);
        } else {
            bot.sendMessage(chatId, "Извините. Вы не являетесь зарегистрированным опекуном.");
        }
    }

    private void askForText(long chatId) {
        awaitingText.put(chatId, true);
    }

    private void askForPhoto(long chatId) {
        //TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        awaitingPhoto.put(chatId, true);
    }

    public void handleTextMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        if (update.hasMessage() && update.getMessage().hasText()) {
            if (isAwaitingText(chatId) && !isTextCreate) {
                report = createReport(chatId, messageText);
                bot.sendMessage(chatId, "Пожалуйста, прикрепите фото:");
            } else if (isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "Необходимо прикрепить фото!");
            } else {
                bot.sendMessage(chatId, "Извините, указанная команда не распознана");
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            if (isTextCreate) {
                askForPhoto(chatId);
                photoReport = createPhotoReport(update);
                finishSendReport(chatId, photoReport); // передаем photoReport явно
            } else if (!isTextCreate && isAwaitingText(chatId)) {
                bot.sendMessage(chatId, "Нужно прикрепить текст!");
            } else if (!isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "На данном этапе ничего прикреплять не нужно!");
            }
        }
    }

    protected boolean isAwaitingText(long chatId) {
        return awaitingText.containsKey(chatId) && awaitingText.get(chatId);
    }

    protected boolean isAwaitingPhoto(long chatId) {
        return awaitingPhoto.containsKey(chatId) && awaitingPhoto.get(chatId);
    }


    private Report createReport(long chatId, String text) {
        Report report = new Report();
        report.setText(text);
        report.setDate(LocalDateTime.now().toLocalDate());
        report.setCheckReport(false);
        report.setUser(userRepository.findByChatId(chatId).get());
        isTextCreate = true;
        return report;
    }

    private PhotoReport createPhotoReport(Update update) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        PhotoSize photo = update.getMessage().getPhoto().get(0);

        PhotoReport photoReport = new PhotoReport();

        // Загрузка данных изображения из Telegram API
        byte[] imageData = bot.downloadFileBytes(photo.getFileId());

        photoReport.setFilePath(generateUniqueFilePath(photo)); // Установливаем уникальный путь
        photoReport.setFileSize((long) imageData.length); // Установливаем размер файла
        photoReport.setMediaType("image/jpeg"); // Можно изменить в зависимости от типа файла
        photoReport.setData(imageData); // Установите данные изображения

        // Связываем с отчетом
        if (report != null) {
            photoReport.setReport(report);
        } else {
            log.error("Report is null when creating PhotoReport");
            // Дополнительная обработка ошибки, если отчет не был создан
        }

        return photoReport;
    }


    private void finishSendReport(long chatId, PhotoReport photoReport) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        bot.sendMessage(chatId, "Отчёт отправлен! Спасибо.");
        reportRepository.save(report);
        photoReportRepository.save(photoReport);
        isTextCreate = false;
        awaitingText.remove(chatId);
        awaitingPhoto.remove(chatId);
    }
    private String generateUniqueFilePath(PhotoSize photo) {
        // Ваш код для генерации уникального пути к файлу
        // Например, можно использовать UUID или текущее время для создания уникального имени файла
        String uniqueFileName = UUID.randomUUID().toString() + ".jpg";
        return Paths.get("D:/Java_projects/DogShelter/photos/", uniqueFileName).toString();
    }
}