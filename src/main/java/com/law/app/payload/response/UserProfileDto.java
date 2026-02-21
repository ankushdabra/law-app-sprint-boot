package com.law.app.payload.response;

import com.law.app.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private UUID id;
    private String username;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String barCouncilEnrollmentNumber;
    private Integer enrollmentYear;
    private String stateBarCouncil;
    private String primaryPracticeArea;
    private Integer yearsOfExperience;
    private String officeCity;
    private String officeState;
    private String lawFirmName;
    private String profilePictureData;
    private String stateBarCouncilCertificateData;
    private List<String> roles;

    public static UserProfileDto fromEntity(UserEntity user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .barCouncilEnrollmentNumber(user.getBarCouncilEnrollmentNumber())
                .enrollmentYear(user.getEnrollmentYear())
                .stateBarCouncil(user.getStateBarCouncil())
                .primaryPracticeArea(user.getPrimaryPracticeArea())
                .yearsOfExperience(user.getYearsOfExperience())
                .officeCity(user.getOfficeCity())
                .officeState(user.getOfficeState())
                .lawFirmName(user.getLawFirmName())
                .profilePictureData(user.getProfilePictureData())
                .stateBarCouncilCertificateData(user.getStateBarCouncilCertificateData())
                .roles(user.getRoles().stream().map(role -> role.getName().name()).toList())
                .build();
    }
}
