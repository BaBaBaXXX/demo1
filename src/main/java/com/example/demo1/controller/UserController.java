package com.example.demo1.controller;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.entity.User;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/profile")
@RequiredArgsConstructor
public class UserController {

     private final UserService userService;


     @PutMapping("/edit")
    public void editUser(UserEditDto dto, Long userId) {
        userService.editUserById(dto, userId);
    }
}
