package com.codefolio.controller;

import com.codefolio.dto.user.NewUserDto;
import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewDto;
import com.codefolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto newUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(newUserDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPreviewDto> previewUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.previewUser(userId));
    }
}
