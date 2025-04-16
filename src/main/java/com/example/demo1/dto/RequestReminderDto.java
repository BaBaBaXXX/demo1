package com.example.demo1.dto;

import java.time.LocalDateTime;

public record RequestReminderDto(
        String query,
        LocalDateTime firstRemind,
        LocalDateTime secondRemind
) {
}
