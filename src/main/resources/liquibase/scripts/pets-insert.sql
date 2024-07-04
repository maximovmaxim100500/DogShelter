-- liquibase formatted sql

-- changeset dsiliukov:${changeset.id.sequence}

-- Добавление данных в таблицу pets для приюта "Дружба"
INSERT INTO pets (name, breed, age, food, shelter_id) VALUES
('Рекс', 'Лабрадор', 3, 'Сухой корм', 1),
('Шарик', 'Такса', 5, 'Консервы', 1),
('Барбос', 'Овчарка', 2, 'Натуральное питание', 1);

-- changeset dsiliukov:${changeset.id.sequence}

-- Добавление данных в таблицу pets для приюта "Четыре лапы"
INSERT INTO pets (name, breed, age, food, shelter_id) VALUES
('Мухтар', 'Хаски', 4, 'Сухой корм', 2),
('Дружок', 'Мопс', 1, 'Консервы', 2),
('Бим', 'Далматин', 3, 'Натуральное питание', 2);