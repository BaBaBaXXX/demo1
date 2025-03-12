package com.example.demo1.controller;

import com.example.demo1.dto.UserEditDto;
import com.example.demo1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserController {

     private final UserService userService;

     @PutMapping("/{userId}")
    public ResponseEntity<String> editUser(UserEditDto dto,
                                           @PathVariable Long userId) {
        userService.editUser(dto, userId);
        return ResponseEntity.ok().body("Edited successfully");
    }
}
