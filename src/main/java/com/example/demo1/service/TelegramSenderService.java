package com.example.demo1.service;

import com.example.demo1.dto.JobReminderDto;
import com.example.demo1.job.telegram.TelegramJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class TelegramSenderService {

    private final Scheduler scheduler;

    public final String TELEGRAM_JOB_PREFIX = "telegramJob_";
    public final String TELEGRAM_TRIGGER_PREFIX = "telegramTrigger_";
    public final String TELEGRAM_GROUP = "telegramGroup";

    @Value("${job.toUser}")
    private String TO_USER;

    @Value("${job.message}")
    private String MESSAGE;

    public void scheduleReminderTelegram(JobReminderDto reminder) {
        Date triggerDate = Date.from(reminder.remind().atZone(ZoneId.systemDefault()).toInstant());
        Long userId = reminder.telegramId();

        JobDetail jobDetail = JobBuilder.newJob(TelegramJob.class)
                .withIdentity(TELEGRAM_JOB_PREFIX + reminder.id(), TELEGRAM_GROUP)
                .usingJobData(TO_USER, userId)
                .usingJobData(MESSAGE, reminder.title() + "\n" + reminder.description())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(TELEGRAM_TRIGGER_PREFIX + reminder.id(), TELEGRAM_GROUP)
                .startAt(triggerDate)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling telegram: {}", e.getMessage());
        }
    }

    public boolean cancelScheduledTelegramMessage(Long reminderId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(TELEGRAM_JOB_PREFIX + reminderId, TELEGRAM_GROUP);

        return scheduler.deleteJob(jobKey);
    }
}
