-- liquibase formatted sql

-- changeset dkhan:1

-- Создание таблицы для хранения информации о волонтерах
CREATE TABLE volunteers (
    id BIGSERIAL PRIMARY KEY,                -- Уникальный идентификатор волонтера
    chat_id BIGINT UNIQUE NOT NULL,          -- Идентификатор чата в Telegram (обязательное поле, уникальное)
    name VARCHAR(255) NOT NULL,              -- Имя волонтера (обязательное поле)
    is_busy BOOLEAN DEFAULT FALSE            -- Статус занятости волонтера (по умолчанию - свободен)
);