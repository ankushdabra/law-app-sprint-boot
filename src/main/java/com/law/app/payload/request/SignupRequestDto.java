package com.law.app.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class SignupRequestDto {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private String fullName;

    private String mobileNumber;

    private String confirmPassword;

    private String barCouncilEnrollmentNumber;

    @Min(1960)
    @Max(2100)
    private Integer enrollmentYear;

    private String stateBarCouncil;

    private String primaryPracticeArea;

    @Min(0)
    @Max(70)
    private Integer yearsOfExperience;

    private String officeCity;

    private String officeState;

    private String lawFirmName;

    private Boolean termsAccepted;
}
