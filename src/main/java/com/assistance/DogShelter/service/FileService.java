package com.assistance.DogShelter.service;

import com.assistance.DogShelter.db.model.AppPhoto;
import com.assistance.DogShelter.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {

    AppPhoto processPhoto(Message telegramMessage);
}
