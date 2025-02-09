package com.example.demo1.job.telegram;


import com.example.demo1.entity.User;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
@Log4j2
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    private final String SUCCESS = "Теперь ваш телеграм аккаунт подключён к уведомлениям!";
    private final String FAIL = "Ваш телеграм аккаунт не привязан к приложению";

    private final UserService userService;

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getUserName();

            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                default: log.info("Unexpected message in chat {}: {}", chatId, messageText);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    //Помечен deprecated, но без него бот не запускается. Думайте.
    @Override
    public String getBotToken() {
        return botToken;
    }


    protected void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        User user = userService.returnUserByTelegram(userName, chatId);
        if (user != null) {
            message.setText(SUCCESS);
        } else {
            message.setText(FAIL);
        }
        try {
            execute(message);
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
}
