-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- Создание таблицы app_photo для хранения фотографий
CREATE TABLE app_photo (
    id SERIAL PRIMARY KEY,                  -- Уникальный идентификатор записи
    telegram_file_id VARCHAR(255),          -- Идентификатор файла в Telegram
    binary_content_id BIGINT,               -- Ссылка на бинарные данные фотографии
    file_size INTEGER,                      -- Размер файла в байтах
    CONSTRAINT fk_binary_content FOREIGN KEY (binary_content_id) REFERENCES binary_content(id)
);