package com.example.demo1.controller;


import com.example.demo1.entity.Reminder;
import com.example.demo1.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1") //todo
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;




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

    @GetMapping("/reminders")
    public Page<Reminder> getRemindersByFilter(String query, LocalDateTime firstRemind, LocalDateTime secondRemind,
                                               @PageableDefault(sort = {"remind", "title"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return reminderService.getReminderByFilter(query, firstRemind, secondRemind, pageable);
    }




}
