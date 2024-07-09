package com.assistance.DogShelter.notification;

import com.assistance.DogShelter.model.Pet;
import com.assistance.DogShelter.model.User;
import com.assistance.DogShelter.repositories.PetRepository;
import com.assistance.DogShelter.repositories.UserRepository;
import com.assistance.DogShelter.service.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyMessageScheduler {
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    private static final Logger logger = LoggerFactory.getLogger(DailyMessageScheduler.class);

    private static final int DAYS_TO_SEND = 30;

    private int daysSent = 0;

    public DailyMessageScheduler(PetRepository petRepository, UserRepository userRepository, TelegramBot telegramBot) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 * * * * *") // Выполняется каждый день в 10:00
    public void sendDailyMessage() {
        String message = "Добрый день! Ждем ваш ежедневный отчет.";
        List<Pet> petList = petRepository.findAll();
        for (Pet pet : petList) {
            if (pet.getUser() != null) {
                User user = userRepository.findById(pet.getUser().getId());
                telegramBot.sendMessage(user.getChatId(), message);
            }
        }
    }
}
