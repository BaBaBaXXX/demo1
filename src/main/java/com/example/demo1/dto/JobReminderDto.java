package com.example.demo1.dto;

import java.time.LocalDateTime;

public record JobReminderDto(
        Long id,
        String title,
        String description,
        LocalDateTime remind,
        String email,
        Long telegramId
) {
}
