package com.law.app.controllers;

import com.law.app.payload.request.ApiRequestDto;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
import com.law.app.payload.response.ApiResponseDto;
import com.law.app.payload.response.JwtResponseDto;
import com.law.app.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto<JwtResponseDto>> authenticateUser(@Valid @RequestBody ApiRequestDto<LoginRequestDto> request) {
        JwtResponseDto jwtResponse = authService.authenticateUser(request.getData());
        return ApiResponseDto.okEntity("Authentication successful", jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<Void>> registerUser(@Valid @RequestBody ApiRequestDto<SignupRequestDto> request) {
        authService.registerUser(request.getData());
        return ApiResponseDto.okEntity("User registered successfully", null);
    }
}

