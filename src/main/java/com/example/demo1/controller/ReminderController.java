package com.example.demo1.controller;


import com.example.demo1.entity.Reminder;
import com.example.demo1.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("${application.properties.endpoint.root}") //todo
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;


    @GetMapping("/{userId}")
    public List<Reminder> getReminders(@PathVariable Long userId) {
        return reminderService.getAllRemindersByUserId(userId);
    }

    @PostMapping
    public String saveReminder(Reminder reminder) {
        reminderService.createReminder(reminder);
        return "Reminder created successfully";
    }

    @DeleteMapping("/{reminderId}")
    public void deleteReminder(@PathVariable Long reminderId) {
        reminderService.deleteReminderById(reminderId);
    }

    @PutMapping("/{reminderId}")
    public String editReminder(@PathVariable Long reminderId, Reminder reminder) {
        reminderService.editReminderById(reminderId, reminder);
        return "Reminder updated successfully";
    }

    @GetMapping("/search")
    public List<Reminder> getRemindersByFilter(String title, String description, LocalDateTime firstRemind, LocalDateTime secondRemind) {
        return reminderService.getReminderByFilter(title, description, firstRemind, secondRemind);
    }




}
