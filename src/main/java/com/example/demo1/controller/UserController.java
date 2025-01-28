package com.example.demo1.controller;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserController {

     private final UserService userService;

     @PutMapping()
    public void editUser(@RequestBody UserEditDto dto) {
        userService.editUser(dto);
    }
}
