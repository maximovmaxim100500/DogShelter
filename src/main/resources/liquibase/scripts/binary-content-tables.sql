-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- Создание таблицы binary_content для хранения файлов
CREATE TABLE binary_content (
    id SERIAL PRIMARY KEY,                  -- Уникальный идентификатор записи
    file_as_array_of_bytes BYTEA NOT NULL   -- Содержимое файла в виде массива байтов
);