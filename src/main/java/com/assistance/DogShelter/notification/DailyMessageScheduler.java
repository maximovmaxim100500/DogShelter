package com.assistance.DogShelter.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DailyMessageScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DailyMessageScheduler.class);
    private static final int DAYS_TO_SEND = 30;

    private int daysSent = 0;

    @Scheduled(cron = "0 0 10 * * *") // Выполняется каждый день в 10:00
    public void sendDailyMessage() {
        if (daysSent < DAYS_TO_SEND) {
            // Логика отправки ежедневного сообщения
            String message = "Добрый день! Ждем ваш ежедневный отчет.";
            logger.info("Message has been sent!");
            // Код для отправки сообщения
            daysSent++;
        }
    }
}
