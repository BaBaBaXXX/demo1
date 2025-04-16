package com.example.demo1.dto;

import java.time.LocalDateTime;

public record ReminderEditDto(
        String title,
        String description,
        LocalDateTime remind
) {
}
