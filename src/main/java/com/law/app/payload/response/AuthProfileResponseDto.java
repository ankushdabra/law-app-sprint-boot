package com.law.app.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthProfileResponseDto {
    private JwtResponseDto accessToken;

    private UserProfileDto profile;
}
