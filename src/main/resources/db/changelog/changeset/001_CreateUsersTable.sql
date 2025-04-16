--liquibase formatted sql

--changeset BaBaBaXXX:1
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(64) UNIQUE NOT NULL,
    telegram VARCHAR(32) UNIQUE
);