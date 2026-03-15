package com.autum.examapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;
    private boolean verified = false;
    private String otp;
    private Long otpExpiryTime;
    private String category;

    public String getResetOtp() {
        return resetOtp;
    }

    public void setResetOtp(String resetOtp) {
        this.resetOtp = resetOtp;
    }

    public Long getResetOtpExpiry() {
        return resetOtpExpiry;
    }

    public void setResetOtpExpiry(Long resetOtpExpiry) {
        this.resetOtpExpiry = resetOtpExpiry;
    }

    private String resetOtp;
    private Long resetOtpExpiry;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    private String role;

    public Long getOtpExpiryTime() {
        return otpExpiryTime;
    }

    public void setOtpExpiryTime(Long otpExpiryTime) {
        this.otpExpiryTime = otpExpiryTime;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}