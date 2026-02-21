package com.law.app.controllers;

import com.law.app.payload.request.ApiRequestDto;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
import com.law.app.payload.request.SignupRequestLegalDto;
import com.law.app.payload.response.ApiResponseDto;
import com.law.app.payload.response.AuthProfileResponseDto;
import com.law.app.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@SecurityRequirements
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponseDto<AuthProfileResponseDto> authenticateUser(@Valid @RequestBody ApiRequestDto<LoginRequestDto> request) {
        AuthProfileResponseDto response = authService.authenticateUser(request.getData());
        return ApiResponseDto.ok("Authentication successful", response);
    }

    @PostMapping(value = "/signup/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Signup User", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = UserSignupMultipartRequest.class), encoding = {@Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE), @Encoding(name = "profilePicture", contentType = "image/*")})))
    public ApiResponseDto<AuthProfileResponseDto> registerUser(@Valid @RequestPart("data") ApiRequestDto<SignupRequestDto> requestDto,
                                                               @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        AuthProfileResponseDto response = authService.registerUser(requestDto.getData(), profilePicture);
        return ApiResponseDto.ok("User registered successfully", response);
    }

    @PostMapping(value = "/signup/advocate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Signup Advocate", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(implementation = LegalSignupMultipartRequest.class), encoding = {@Encoding(name = "data", contentType = MediaType.APPLICATION_JSON_VALUE), @Encoding(name = "profilePicture", contentType = "image/*")})))
    public ApiResponseDto<AuthProfileResponseDto> registerAdvocate(@Valid @RequestPart("data") ApiRequestDto<SignupRequestLegalDto> requestDto,
                                                                   @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture) {
        AuthProfileResponseDto response = authService.registerLegal(requestDto.getData(), profilePicture);
        return ApiResponseDto.ok("Advocate registered successfully", response);
    }

    @Schema(name = "UserSignupMultipartRequest")
    private static class UserSignupMultipartRequest {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        public ApiRequestDto<SignupRequestDto> data;

        @Schema(type = "string", format = "binary")
        public String profilePicture;
    }

    @Schema(name = "LegalSignupMultipartRequest")
    private static class LegalSignupMultipartRequest {
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        public ApiRequestDto<SignupRequestLegalDto> data;

        @Schema(type = "string", format = "binary")
        public String profilePicture;
    }
}
