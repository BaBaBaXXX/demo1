package com.example.demo1.service;

import com.example.demo1.entity.Reminder;
import com.example.demo1.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReminderService {

    public void createReminder(Reminder reminder);

    public List<Reminder> getAllRemindersByUserId(Long userId);

    public void deleteReminderById(Long reminderId);

    public void editReminderById(Long reminderId, Reminder reminder);

    public List<Reminder> getReminderByFilter(String title, String description, LocalDateTime firstRemind, LocalDateTime lastRemind);




}
