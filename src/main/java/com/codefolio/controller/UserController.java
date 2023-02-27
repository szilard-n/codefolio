package com.codefolio.controller;

import com.codefolio.dto.user.UserDto;
import com.codefolio.dto.user.UserPreviewResponse;
import com.codefolio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPreviewResponse> previewUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.previewUser(userId));
    }

    @GetMapping("/validate/{credential}")
    public ResponseEntity<Void> checkCredentialValidity(@PathVariable String credential) {
        userService.checkCredentialValidity(credential);
        return ResponseEntity.ok().build();
    }
}
