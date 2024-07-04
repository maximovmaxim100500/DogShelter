-- liquibase formatted sql

-- changeset mmaksimov:${changeset.id.sequence}

-- Создание таблицы для хранения информации о директории
CREATE TABLE drive_directory (
    id BIGSERIAL PRIMARY KEY,       -- Уникальный идентификатор директории
    name VARCHAR(255) NOT NULL,     -- Название директории
    path VARCHAR(255) NOT NULL      -- Путь к директории
);

-- changeset dsiliukov:${changeset.id.sequence}

-- preConditions
-- precondition onFail: MARK_RAN
-- tableExists tableName: shelters

-- Добавление столбца shelter_id для связи с таблицей shelters
ALTER TABLE drive_directory
    ADD COLUMN shelter_id BIGINT,
    ADD CONSTRAINT fk_shelter_id
        FOREIGN KEY (shelter_id)
        REFERENCES shelters (id);