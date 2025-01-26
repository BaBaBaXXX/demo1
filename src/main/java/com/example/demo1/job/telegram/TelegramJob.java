package com.example.demo1.job.telegram;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@RequiredArgsConstructor
@Log4j2
public class TelegramJob extends QuartzJobBean {

    private final TelegramLongPollingBot bot;

    @Value("${job.toUser}")
    private String TO_USER;

    @Value("${job.message}")
    private String MESSAGE;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Long toUser = context.getMergedJobDataMap().getLong(TO_USER);
        String message = context.getMergedJobDataMap().getString(MESSAGE);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(toUser);
        sendMessage.setText(message);

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Failed to send message: {}", e.getMessage());
        }
    }
}
