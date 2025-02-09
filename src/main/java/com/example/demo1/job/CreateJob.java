package com.example.demo1.job;

import com.example.demo1.dto.JobReminderDto;
import com.example.demo1.mapper.ReminderMapper;
import com.example.demo1.repository.ReminderRepository;
import com.example.demo1.service.EmailSenderService;
import com.example.demo1.service.TelegramSenderService;
import com.example.demo1.util.MailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
@Transactional
@Log4j2
public class CreateJob {

    private final Scheduler scheduler;
    private final ReminderRepository reminderRepository;

    private final EmailSenderService emailSenderService;
    private final TelegramSenderService telegramSenderService;
    private final ReminderMapper reminderMapper;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jobListCreator();
    }


    public void jobListCreator () {
        try {
            Set<JobKey> jobKeyList = scheduler.getJobKeys(GroupMatcher.anyGroup());
            Set<String> jobKeyNames = new HashSet<>();
            for (JobKey jobKey : jobKeyList) {
                jobKeyNames.add(jobKey.getName());
            }

            List<JobReminderDto> reminders = reminderMapper.toJobReminderDtoList((reminderRepository.findAllFutureReminders()));

            for (JobReminderDto reminder : reminders) {
                if (!jobKeyNames.contains(emailSenderService.EMAIL_JOB_PREFIX + reminder.id())) {
                    emailSenderService.scheduleEmail(MailType.REMINDER, reminder);
                }
                if (!jobKeyNames.contains(telegramSenderService.TELEGRAM_JOB_PREFIX + reminder.id())) {
                    telegramSenderService.scheduleReminderTelegram(reminder);
                }
            }
        } catch (SchedulerException e) {
            log.error("Failed to create job list: {}", e.getMessage());
        }
    }
}
