package com.example.demo1.service;

import com.example.demo1.dto.JobReminderDto;
import com.example.demo1.dto.ReminderEditDto;
import com.example.demo1.dto.RequestReminderDto;
import com.example.demo1.entity.Reminder;
import com.example.demo1.entity.User;
import com.example.demo1.mapper.ReminderMapper;
import com.example.demo1.repository.ReminderRepository;
import com.example.demo1.repository.UserRepository;
import com.example.demo1.util.MailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final TelegramSenderService telegramSenderService;
    private final ReminderMapper reminderMapper;

    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);

        JobReminderDto jobReminderDto = reminderMapper.toJobReminderDto(reminder);
        emailSenderService.scheduleEmail(MailType.REMINDER, jobReminderDto);
        telegramSenderService.scheduleReminderTelegram(jobReminderDto);
    }

    public void deleteReminderById(Long reminderId) {
        reminderRepository.deleteById(reminderId);
        cancelEmail(reminderId);
        cancelTelegramMessage(reminderId);
    }

    public void editReminder(ReminderEditDto reminder, Long reminderId) {
        Optional<Reminder> oldReminder = reminderRepository.findById(reminderId);
        oldReminder.ifPresent(editReminder -> {
            editReminder.setTitle(reminder.title());
            editReminder.setDescription(reminder.description());
            editReminder.setRemind(reminder.remind());
            reminderRepository.save(editReminder);
        });
    }

    // Если код доходит до сюда, то findByEmail никогда не должен возвращать null. Вроде.
    public Page<Reminder> getReminderByFilter(RequestReminderDto requestReminderDto,
                                              Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).get();


        return reminderRepository.findAllWithFilter(requestReminderDto, pageable, user);
    }

    private void cancelEmail(Long reminderId) {
        try {
            boolean isCancelled = emailSenderService.cancelScheduledEmail(reminderId);
            if (isCancelled) {
                log.info("Scheduled email for reminder {} was successfully cancelled", reminderId);
            } else {
                log.warn("No scheduled email found for reminder {}", reminderId);
            }
        } catch (Exception e) {
            log.error("Error while deleting scheduled email for reminder {}, {}", reminderId, e.getMessage());
        }
    }

    private void cancelTelegramMessage(Long reminderId) {
        try {
            boolean isCancelled = telegramSenderService.cancelScheduledTelegramMessage(reminderId);
            if (isCancelled) {
                log.info("Scheduled telegram message for reminder {} was successfully cancelled", reminderId);
            } else {
                log.warn("No scheduled telegram message found for reminder {}", reminderId);
            }
        } catch (Exception e) {
            log.error("Error while deleting scheduled telegram message for reminder {}, {}", reminderId, e.getMessage());
        }
    }
}
