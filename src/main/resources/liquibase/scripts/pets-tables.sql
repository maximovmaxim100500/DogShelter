-- liquibase formatted sql

-- changeset dsiliukov:1

-- Создание таблицы для хранения информации о питомцах
CREATE TABLE pets (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор питомца
    name VARCHAR(255) NOT NULL,     -- Имя питомца
    breed VARCHAR(255) NOT NULL,    -- Порода питомца
    age INT NOT NULL CHECK (age >= 0),  -- Возраст питомца (не может быть отрицательным)
    food VARCHAR(255)               -- Предпочитаемая еда питомца
);
