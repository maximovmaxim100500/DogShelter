-- liquibase formatted sql

-- changeset dsiliukov:2

-- Создание таблицы для хранения аватаров питомцев
CREATE TABLE pet_avatars (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор аватара
    file_path VARCHAR(255) NOT NULL, -- Путь к файлу аватара
    file_size BIGINT NOT NULL,      -- Размер файла аватара
    media_type VARCHAR(255) NOT NULL, -- Тип медиа файла аватара
    data BYTEA NOT NULL,            -- Данные файла аватара
    pet_id BIGINT NOT NULL,         -- Идентификатор питомца, ссылка на таблицу pets
    CONSTRAINT fk_pet
        FOREIGN KEY (pet_id)
        REFERENCES pets (id)
        ON DELETE CASCADE
);