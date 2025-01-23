package com.example.demo1.service;

import com.example.demo1.entity.Reminder;
import com.example.demo1.entity.User;
import com.example.demo1.repository.ReminderRepository;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;


    public void createReminder(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public void deleteReminderById(Long reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    //TODO 12.01 доделать редакт через DTO
    public void editReminderById(Long reminderId, Reminder reminder) {
        reminderRepository.save(reminder);
    }


    // Если код доходит до сюда, то findByEmail никогда не должен возвращать null. Вроде.
    public Page<Reminder> getReminderByFilter(String query, LocalDateTime firstRemind, LocalDateTime lastRemind, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email).get();


        return reminderRepository.findAllWithFilter(query, firstRemind, lastRemind, pageable, user);
    }



}
