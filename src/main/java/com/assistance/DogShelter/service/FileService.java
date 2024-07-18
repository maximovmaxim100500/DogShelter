package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {

    AppPhoto processPhoto(Message telegramMessage);
}
