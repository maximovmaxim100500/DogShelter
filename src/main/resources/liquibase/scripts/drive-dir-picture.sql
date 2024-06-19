-- liquibase formatted sql

-- changeset mmaximov:2

-- Создание таблицы для хранения схем проезда к приютам
CREATE TABLE drive_dir_picture (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор схемы
    file_path VARCHAR(255) NOT NULL, -- Путь к файлу схемы
    file_size BIGINT NOT NULL,      -- Размер файла схемы
    media_type VARCHAR(255) NOT NULL, -- Тип медиа файла схемы
    data BYTEA NOT NULL,            -- Данные файла схемы
    shelter_id BIGINT NOT NULL,         -- Идентификатор приюта, ссылка на таблицу shelters
    CONSTRAINT fk_shelters
        FOREIGN KEY (shelter_id)
        REFERENCES shelters (id)
        ON DELETE CASCADE
);