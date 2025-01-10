package com.example.demo1.service;

import com.example.demo1.entity.Reminder;
import com.example.demo1.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;


    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public void deleteReminderById(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public void editReminderById(Long reminderId, Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public Page<Reminder> getReminderByFilter(String query, LocalDateTime firstRemind, LocalDateTime lastRemind, Pageable pageable) {
        return reminderRepository.findAllWithFilter(query, firstRemind, lastRemind, pageable);
    }



}
