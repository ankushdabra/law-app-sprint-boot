package com.law.app.services;

import com.law.app.entities.RoleEntity;
import com.law.app.entities.Roles;
import com.law.app.entities.UserEntity;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
import com.law.app.payload.request.SignupRequestLegalDto;
import com.law.app.payload.response.AuthProfileResponseDto;
import com.law.app.payload.response.UserProfileDto;
import com.law.app.repository.RoleRepository;
import com.law.app.repository.UserRepository;
import com.law.app.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthProfileResponseDto authenticateUser(LoginRequestDto loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        return buildAuthResponse(user, loginRequest.getPassword());
    }

    public AuthProfileResponseDto registerUser(SignupRequestDto data) {
        validateUniqueUser(data.getUsername(), data.getEmail());

        UserEntity user = UserEntity.builder()
                .username(data.getUsername())
                .fullName(data.getFullName())
                .email(data.getEmail())
                .mobileNumber(data.getMobileNumber())
                .password(encoder.encode(data.getPassword()))
                .roles(Set.of(getRoleOrThrow(Roles.ROLE_USER)))
                .build();

        UserEntity savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser, data.getPassword());
    }

    public AuthProfileResponseDto registerLegal(SignupRequestLegalDto data) {
        validateUniqueUser(data.getUsername(), data.getEmail());
        UserEntity user = UserEntity.builder()
                .username(data.getUsername())
                .fullName(data.getFullName())
                .email(data.getEmail())
                .mobileNumber(data.getMobileNumber())
                .password(encoder.encode(data.getPassword()))
                .barCouncilEnrollmentNumber(data.getBarCouncilEnrollmentNumber())
                .enrollmentYear(data.getEnrollmentYear())
                .stateBarCouncil(data.getStateBarCouncil())
                .primaryPracticeArea(data.getPrimaryPracticeArea())
                .yearsOfExperience(data.getYearsOfExperience())
                .officeCity(data.getOfficeCity())
                .officeState(data.getOfficeState())
                .lawFirmName(data.getLawFirmName())
                .roles(Set.of(getRoleOrThrow(Roles.ROLE_LEGAL)))
                .build();

        UserEntity savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser, data.getPassword());
    }


    private AuthProfileResponseDto buildAuthResponse(UserEntity user, String rawPassword) {

        return AuthProfileResponseDto.builder()
                .accessToken(jwtUtils.buildJwtResponse(user.getUsername(), rawPassword))
                .profile(UserProfileDto.fromEntity(user))
                .build();
    }

    private void validateUniqueUser(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Error: Username is already taken!");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Error: Email is already in use!");
        }
    }

    private RoleEntity getRoleOrThrow(Roles role) {
        return roleRepository.findByName(role)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    }


}
