--liquibase formatted sql

--changeset BaBaBaXXX:1
INSERT INTO users(email) VALUES
                             ('kfne95@mail.ru'),
                             ('7556e@mail.ru');
