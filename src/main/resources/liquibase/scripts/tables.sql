-- liquibase formatted sql

-- changeset dkhan:1

CREATE TABLE volunteers(
id BIGSERIAL primary key,
chat_id BIGINT,
name VARCHAR
)