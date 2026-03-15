package com.autum.examapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class UserRequestDTO {
    public @NotBlank(message = "Name cannot be empty") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Name cannot be empty") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Password cannot be empty") @Size(min = 5, message = "Password must be at least 5 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password cannot be empty") @Size(min = 5, message = "Password must be at least 5 characters") String password) {
        this.password = password;
    }

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;
}