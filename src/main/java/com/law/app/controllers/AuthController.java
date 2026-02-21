package com.law.app.controllers;

import com.law.app.entities.Roles;
import com.law.app.payload.request.ApiRequestDto;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
import com.law.app.payload.request.SignupRequestLegalDto;
import com.law.app.payload.response.ApiResponseDto;
import com.law.app.payload.response.AuthProfileResponseDto;
import com.law.app.services.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ApiResponseDto<AuthProfileResponseDto> authenticateUser(@Valid @RequestBody ApiRequestDto<LoginRequestDto> request) {
        AuthProfileResponseDto response = authService.authenticateUser(request.getData());
        return ApiResponseDto.ok("Authentication successful", response);
    }

    @PostMapping("/signup/user")
    public ApiResponseDto<AuthProfileResponseDto> registerUser(@Valid @RequestBody ApiRequestDto<SignupRequestDto> request) {
        AuthProfileResponseDto response = authService.registerUser(request.getData());
        return ApiResponseDto.ok("User registered successfully", response);
    }

    @PostMapping("/signup/advocate")
    public ApiResponseDto<AuthProfileResponseDto> registerAdvocate(@Valid @RequestBody ApiRequestDto<SignupRequestLegalDto> request) {
        AuthProfileResponseDto response = authService.registerLegal(request.getData());
        return ApiResponseDto.ok("Advocate registered successfully", response);
    }
}
