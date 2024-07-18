package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.AppPhoto;
import com.assistance.DogShelter.db.model.PhotoReport;
import com.assistance.DogShelter.db.model.Report;
import com.assistance.DogShelter.db.repository.PhotoReportRepository;
import com.assistance.DogShelter.db.repository.ReportRepository;
import com.assistance.DogShelter.db.repository.UserRepository;
import com.assistance.DogShelter.exceptions.UploadFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private FileService fileService;

    private Map<Long, Boolean> awaitingText = new HashMap<>();
    private Map<Long, Boolean> awaitingPhoto = new HashMap<>();

    private Report report;
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
                askForPhoto(chatId);
            } else if (isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "Необходимо прикрепить фото!");
            } else {
                bot.sendMessage(chatId, "Извините, указанная команда не распознана");
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            if (isAwaitingPhoto(chatId)) {
                handlePhotoMessage(update);
            } else if (!isTextCreate && isAwaitingText(chatId)) {
                bot.sendMessage(chatId, "Нужно прикрепить текст!");
            } else if (!isTextCreate && !isAwaitingPhoto(chatId)) {
                bot.sendMessage(chatId, "На данном этапе ничего прикреплять не нужно!");
            }
        }
    }

    private void handlePhotoMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);

        try {
            var appPhoto = fileService.processPhoto(update.getMessage());
            var photoReport = createPhotoReport(appPhoto);

            finishSendReport(chatId, photoReport);
        } catch (UploadFileException e) {
            log.error("Ошибка при загрузке файла", e);
            bot.sendMessage(chatId, "К сожалению, загрузка фото не удалась. Повторите попытку позже.");
        }
    }

    private PhotoReport createPhotoReport(AppPhoto appPhoto) {
        PhotoReport photoReport = new PhotoReport();
        photoReport.setFilePath(appPhoto.getTelegramFileId()); // Используйте идентификатор файла как путь
        photoReport.setFileSize(appPhoto.getFileSize().longValue()); // Размер файла
        photoReport.setMediaType("image/jpeg"); // Можно изменить в зависимости от типа файла
        photoReport.setData(appPhoto.getBinaryContent().getFileAsArrayOfBytes()); // Данные изображения
        photoReport.setReport(report);
        return photoReport;
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

    private void finishSendReport(long chatId, PhotoReport photoReport) {
        TelegramBot bot = applicationContext.getBean(TelegramBot.class);
        bot.sendMessage(chatId, "Отчёт отправлен! Спасибо.");
        reportRepository.save(report);
        photoReportRepository.save(photoReport);
        isTextCreate = false;
        awaitingText.remove(chatId);
        awaitingPhoto.remove(chatId);
    }
}