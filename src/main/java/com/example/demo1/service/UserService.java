package com.example.demo1.service;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void editUser(UserEditDto dto) {
        userRepository.findById(dto.id()).ifPresent(user -> {
            user.setTelegram(dto.telegram());
            userRepository.save(user);
        });

    }
}
