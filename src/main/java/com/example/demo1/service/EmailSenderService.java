package com.example.demo1.service;


import com.example.demo1.dto.JobReminderDto;
import com.example.demo1.job.email.EmailJob;
import com.example.demo1.util.MailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailSenderService {

    private final Scheduler scheduler;

    public final String EMAIL_JOB_PREFIX = "emailJob_";
    public final String EMAIL_TRIGGER_PREFIX = "emailTrigger_";
    public final String EMAIL_GROUP = "emailGroup";

    @Value("${job.toEmail}")
    private String TO_EMAIL;

    @Value("${job.subject}")
    private String SUBJECT;

    @Value("${job.body}")
    private String BODY;

    public void scheduleEmail (MailType mailType,
                           JobReminderDto reminder) {
        switch(mailType) {
            case REMINDER -> scheduleReminderEmail(reminder);
            default -> log.error("Unexpected value: {}", mailType);
        }
    }

    private void scheduleReminderEmail(JobReminderDto reminder) {
        Date triggerDate = Date.from(reminder.remind().atZone(ZoneId.systemDefault()).toInstant());
        String email = reminder.email();

        JobDetail jobDetail = JobBuilder.newJob(EmailJob.class)
                .withIdentity(EMAIL_JOB_PREFIX + reminder.id(), EMAIL_GROUP)
                .usingJobData(TO_EMAIL, email)
                .usingJobData(SUBJECT, reminder.title())
                .usingJobData(BODY, reminder.description())
                .build();


        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(EMAIL_TRIGGER_PREFIX + reminder.id(), EMAIL_GROUP)
                .startAt(triggerDate)
                .build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("Error scheduling email: {}", e.getMessage());
        }
    }

    public boolean cancelScheduledEmail (Long reminderId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(EMAIL_JOB_PREFIX + reminderId, EMAIL_GROUP);

        return scheduler.deleteJob(jobKey);
    }
}
