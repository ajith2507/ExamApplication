package com.autum.examapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


public class OtpVerificationDTO {

    @NotBlank(message = "Email cannot be empty")
    private String email;

    public @NotBlank(message = "Email cannot be empty") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be empty") String email) {
        this.email = email;
    }

    public @NotBlank(message = "OTP cannot be empty") String getOtp() {
        return otp;
    }

    public void setOtp(@NotBlank(message = "OTP cannot be empty") String otp) {
        this.otp = otp;
    }

    @NotBlank(message = "OTP cannot be empty")
    private String otp;
}