package com.autum.examapp.controller;

import com.autum.examapp.dto.LoginRequestDTO;
import com.autum.examapp.dto.OtpVerificationDTO;
import com.autum.examapp.dto.UserDTO;
import com.autum.examapp.dto.UserRequestDTO;
import com.autum.examapp.repository.UserRepository;
import com.autum.examapp.service.EmailService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import com.autum.examapp.model.User;
import com.autum.examapp.service.UserService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/save")
    public UserDTO saveUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return userService.saveUser(userRequestDTO);
    }
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "User deleted successfully";
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Integer id,
                           @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @GetMapping("/test-email")
    public String testEmail() {

        emailService.sendEmail(
                "ajith05010@gmail.com",
                "test email",
                "Email sending is working successfully!"
        );

        return "Email sent successfully";
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerificationDTO dto) {
        return userService.verifyOtp(dto);
    }
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.login(loginRequestDTO);
    }

    @GetMapping("/encode/{raw}")
    public String encodePassword(@PathVariable String raw) {
        return passwordEncoder.encode(raw);
    }
    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String email) {
        return userService.resendOtp(email);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        try {
            String result = userService.forgotPassword(email);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending OTP: " + e.getMessage());
        }
    }
    @PostMapping("/verify-reset-otp")
    public String verifyResetOtp(@RequestParam String email,
                                 @RequestParam String otp) {

        return userService.verifyResetOtp(email, otp);
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String otp,
                                @RequestParam String newPassword) {

        userService.verifyResetOtp(email, otp);
        return userService.resetPassword(email, newPassword);
    }

    @PutMapping("/update")
    public User updateProfile(@RequestBody User updatedUser){

        User user = userRepository.findByEmail(updatedUser.getEmail());

        user.setName(updatedUser.getName());


        return userRepository.save(user);
    }
    @GetMapping("/profile")
    public User getProfile(@RequestParam String email){
        return userRepository.findByEmail(email);
    }
}