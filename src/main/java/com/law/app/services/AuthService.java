package com.law.app.services;

import com.law.app.entities.RoleEntity;
import com.law.app.entities.Roles;
import com.law.app.entities.UserEntity;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
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

    public AuthProfileResponseDto registerUser(SignupRequestDto signUpRequest, Roles roleUser) {
        validateUniqueUser(signUpRequest.getUsername(), signUpRequest.getEmail());
        SignupRequestDto.AdvocateProfile advocateProfile = signUpRequest.getAdvocateProfile();

        UserEntity user = UserEntity.builder()
                .username(signUpRequest.getUsername())
                .fullName(signUpRequest.getFullName())
                .email(signUpRequest.getEmail())
                .mobileNumber(signUpRequest.getMobileNumber())
                .password(encoder.encode(signUpRequest.getPassword()))
                .barCouncilEnrollmentNumber(advocateProfile != null ? advocateProfile.getBarCouncilEnrollmentNumber() : null)
                .enrollmentYear(advocateProfile != null ? advocateProfile.getEnrollmentYear() : null)
                .stateBarCouncil(advocateProfile != null ? advocateProfile.getStateBarCouncil() : null)
                .primaryPracticeArea(advocateProfile != null ? advocateProfile.getPrimaryPracticeArea() : null)
                .yearsOfExperience(advocateProfile != null ? advocateProfile.getYearsOfExperience() : null)
                .officeCity(advocateProfile != null ? advocateProfile.getOfficeCity() : null)
                .officeState(advocateProfile != null ? advocateProfile.getOfficeState() : null)
                .lawFirmName(advocateProfile != null ? advocateProfile.getLawFirmName() : null)
                .roles(Set.of(getRoleOrThrow(roleUser)))
                .build();

        UserEntity savedUser = userRepository.save(user);

        return buildAuthResponse(savedUser, signUpRequest.getPassword());
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
