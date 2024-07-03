-- liquibase formatted sql

-- changeset mmaksimov:1

-- Создание таблицы для хранения фотографий отчетов
CREATE TABLE photo_report (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор фото
    file_path VARCHAR(255) NOT NULL, -- Путь к файлу фото
    file_size BIGINT NOT NULL,      -- Размер файла фото
    media_type VARCHAR(255) NOT NULL, -- Тип медиа файла фото
    data BYTEA NOT NULL,            -- Данные файла фото
    report_id BIGINT NOT NULL,         -- Идентификатор отчета, ссылка на таблицу reports
    CONSTRAINT fk_reports
        FOREIGN KEY (report_id)
        REFERENCES reports (id)
        ON DELETE CASCADE
);