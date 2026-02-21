package com.law.app.services;

import com.law.app.entities.RoleEntity;
import com.law.app.entities.Roles;
import com.law.app.entities.UserEntity;
import com.law.app.payload.request.LoginRequestDto;
import com.law.app.payload.request.SignupRequestDto;
import com.law.app.payload.response.JwtResponseDto;
import com.law.app.repository.RoleRepository;
import com.law.app.repository.UserRepository;
import com.law.app.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public JwtResponseDto authenticateUser(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return JwtResponseDto.builder().accessToken(jwt).username(userDetails.getUsername()).email(userDetails.getEmail()).roles(roles).build();
    }

    public void registerUser(SignupRequestDto signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        Set<String> strRoles = signUpRequest.getRole();
        Set<RoleEntity> roles = new HashSet<>();

        if (strRoles == null) {
            RoleEntity userRole = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equalsIgnoreCase("legal")) {
                    RoleEntity legalRole = roleRepository.findByName(Roles.ROLE_LEGAL).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(legalRole);
                } else {
                    RoleEntity userRole = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        UserEntity user = UserEntity.builder().username(signUpRequest.getUsername()).email(signUpRequest.getEmail()).password(encoder.encode(signUpRequest.getPassword())).roles(roles).build();

        userRepository.save(user);
    }
}

