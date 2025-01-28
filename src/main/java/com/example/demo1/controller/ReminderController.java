package com.example.demo1.controller;


import com.example.demo1.dto.ReminderEditDto;
import com.example.demo1.dto.RequestReminderDto;
import com.example.demo1.entity.Reminder;
import com.example.demo1.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @PostMapping
    public ResponseEntity<String> saveReminder(@RequestBody Reminder reminder) {
        reminderService.createReminder(reminder);
        return ResponseEntity.status(CREATED).body("Reminder created");
    }

    @DeleteMapping("/{reminderId}")
    public void deleteReminder(@PathVariable Long reminderId) {
        reminderService.deleteReminderById(reminderId);
    }

    @PutMapping("/{reminderId}")
    public ResponseEntity<String> editReminder(ReminderEditDto reminder, @PathVariable Long reminderId) {
        reminderService.editReminder(reminder, reminderId);
        return ResponseEntity.ok().body("Reminder updated");
    }

    @GetMapping
    public Page<Reminder> getRemindersByFilter(RequestReminderDto requestReminderDto,
                                               @PageableDefault(sort = {"remind", "title"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return reminderService.getReminderByFilter(requestReminderDto, pageable);
    }




}
