package com.law.app.controllers;

import com.law.app.payload.request.ApiRequest;
import com.law.app.payload.request.LoginRequest;
import com.law.app.payload.request.SignupRequest;
import com.law.app.payload.response.ApiResponse;
import com.law.app.payload.response.JwtResponse;
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
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody ApiRequest<LoginRequest> request) {
        JwtResponse jwtResponse = authService.authenticateUser(request.getData());
        return ApiResponse.okEntity("Authentication successful", jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> registerUser(@Valid @RequestBody ApiRequest<SignupRequest> request) {
        authService.registerUser(request.getData());
        return ApiResponse.okEntity("User registered successfully", null);
    }
}
