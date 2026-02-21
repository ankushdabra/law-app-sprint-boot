package com.law.app.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDto {
    private String accessToken;
    @Builder.Default
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private List<String> roles;
}

