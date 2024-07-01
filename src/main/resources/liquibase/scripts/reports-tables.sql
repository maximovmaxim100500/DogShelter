-- liquibase formatted sql

-- changeset mmaksimov:1

-- Создание таблицы для хранения информации об отчетах
CREATE TABLE reports (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор отчета
    text TEXT NOT NULL,             -- Текст отчета
    date DATE NOT NULL              -- Дата отчета
);
