package com.example.demo1.service;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;


@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void editUser(UserEditDto dto, Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            if (dto.telegram() != null) {
                user.setTelegram(dto.telegram());
            }
            userRepository.save(user);
        });
    }

    public User returnUserByTelegram(String telegram, Long userId) {
        Optional<User> optionalUser = userRepository.findByTelegram(telegram);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!Objects.equals(user.getTelegramId(), userId)) {
                user.setTelegramId(userId);
                userRepository.save(user);
            }
            return user;
        }
        return null;
    }
}
