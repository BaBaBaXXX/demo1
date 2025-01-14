package com.example.demo1.service;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.entity.User;
import com.example.demo1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void createUser(String email) {
        User user = new User();
        user.setEmail(email);
        userRepository.save(user);
    }

    public void editUserById(UserEditDto dto, Long id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setTelegram(dto.getTelegram());
            userRepository.save(user);
        });

    }
}
