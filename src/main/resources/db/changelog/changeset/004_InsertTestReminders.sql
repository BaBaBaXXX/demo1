--liquibase formatted sql

--changeset BaBaBaXXX:1
INSERT INTO reminder(title, description, remind, user_id) VALUES
                                                     ('Тест ачуметь',
                                                      'Описание тест ачуметь',
                                                      NOW() + interval '1 minute',
                                                      1),

                                                     ('Тест2 ачуметь',
                                                      'Описание2 тест ачуметь',
                                                      NOW() + interval '2 minute',
                                                      1),

                                                     ('Тест3 ачуметь',
                                                      'Описание3 тест ачуметь',
                                                      NOW() + interval '3 minute',
                                                      1),

                                                     ('Тест4 ачуметь',
                                                      'Описание4 тест ачуметь',
                                                      NOW() + interval '4 minute',
                                                      1),

                                                     ('Тест5 ачуметь',
                                                      'Описание5 тест ачуметь',
                                                      NOW() + interval '5 minute',
                                                      1);

