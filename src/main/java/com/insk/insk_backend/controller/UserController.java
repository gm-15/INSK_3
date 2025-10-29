package com.insk.insk_backend.controller;

import com.insk.insk_backend.dto.UserDto;
import com.insk.insk_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignUpResponse> signup(@Valid @RequestBody UserDto.SignUpRequest request) {
        UserDto.SignUpResponse response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginResponse> login(@Valid @RequestBody UserDto.LoginRequest request) {
        // UserService의 login 메소드를 호출하여 JWT 토큰을 받아옵니다.
        UserDto.LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

}