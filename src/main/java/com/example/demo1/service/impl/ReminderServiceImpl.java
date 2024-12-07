package com.example.demo1.service.impl;

import com.example.demo1.entity.Reminder;
import com.example.demo1.repository.ReminderRepository;
import com.example.demo1.service.ReminderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReminderServiceImpl implements ReminderService {

    private final ReminderRepository reminderRepository;


    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public List<Reminder> getAllRemindersByUserId(Long userId) {
        return reminderRepository.findAllByUserId(userId);
    }

    public void deleteReminderById(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public void editReminderById(Long reminderId, Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public List<Reminder> getReminderByFilter(String query, LocalDateTime firstRemind, LocalDateTime lastRemind) {
        return reminderRepository.findAllWithFilter(query, firstRemind, lastRemind);
    }
}
