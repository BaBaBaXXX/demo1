package com.example.demo1.job.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class EmailJob extends QuartzJobBean {

    private final JavaMailSender mailSender;

    @Value("${job.toEmail}")
    private String TO_EMAIL;

    @Value("${job.subject}")
    private String SUBJECT;

    @Value("${job.body}")
    private String BODY;

    //Вроде такие хардкоды использующиеся больше 1 раза, стоит закидывать в yaml файл, но я не уверен
    @Override
    protected void executeInternal(JobExecutionContext context) {
        String toEmail = context.getMergedJobDataMap().getString(TO_EMAIL);
        String subject = context.getMergedJobDataMap().getString(SUBJECT);
        String body = context.getMergedJobDataMap().getString(BODY);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
