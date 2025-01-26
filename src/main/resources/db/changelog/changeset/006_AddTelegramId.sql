--liquibase formatted sql

--changeset BaBaBaXXX:1
ALTER TABLE IF EXISTS users
    ADD COLUMN telegramId BIGINT;