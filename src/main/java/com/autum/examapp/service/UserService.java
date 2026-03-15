package com.autum.examapp.service;

import com.autum.examapp.dto.LoginRequestDTO;
import com.autum.examapp.dto.OtpVerificationDTO;
import com.autum.examapp.dto.UserDTO;
import com.autum.examapp.dto.UserRequestDTO;
import com.autum.examapp.exception.InvalidOtpException;
import com.autum.examapp.exception.OtpExpiredException;
import com.autum.examapp.exception.OtpNotRequestedException;
import com.autum.examapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.autum.examapp.exception.UserNotFoundException;

import com.autum.examapp.model.User;
import com.autum.examapp.repository.UserRepository;
import com.autum.examapp.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;



    public UserDTO saveUser(UserRequestDTO userRequestDTO) {

        // Check if user already exists
        User existingUser = userRepository.findByEmail(userRequestDTO.getEmail());

        if (existingUser != null) {

            // If already verified → stop registration
            if (existingUser.isVerified()) {
                throw new RuntimeException("Account already exists. Please login.");
            }

            // If NOT verified → resend OTP instead of creating new account
            String otp = generateOtp();
            existingUser.setOtp(otp);
            existingUser.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000));

            userRepository.save(existingUser);

            emailService.sendEmail(
                    existingUser.getEmail(),
                    "Your OTP Verification Code",
                    "Your OTP is: " + otp + "\nIt expires in 5 minutes."
            );

            UserDTO dto = new UserDTO();
            dto.setId(existingUser.getId());
            dto.setName(existingUser.getName());
            dto.setEmail(existingUser.getEmail());

            return dto;
        }

        // New user registration
        User user = new User();

        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setVerified(false);
        user.setRole("ROLE_USER");

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000));

        User savedUser = userRepository.save(user);

        emailService.sendEmail(
                savedUser.getEmail(),
                "Your OTP Verification Code",
                "Your OTP is: " + savedUser.getOtp() + "\nIt expires in 5 minutes."
        );

        UserDTO userDTO = new UserDTO();
        userDTO.setId(savedUser.getId());
        userDTO.setName(savedUser.getName());
        userDTO.setEmail(savedUser.getEmail());

        return userDTO;
    }
    public List<UserDTO> getAllUsers() {

        // Step 1: get all users from database
        List<User> users = userRepository.findAll();

        // Step 2: create empty DTO list
        List<UserDTO> dtoList = new ArrayList<>();

        // Step 3: convert each user into DTO
        for (User user : users) {

            UserDTO dto = convertToDTO(user);

            dtoList.add(dto);
        }

        // Step 4: return DTO list
        return dtoList;
    }
    public UserDTO getUserById(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return convertToDTO(user);
    }
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
    public User updateUser(Integer id, User updatedUser) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());

            return userRepository.save(existingUser);
        }

        return null;
    }

    public UserDTO convertToDTO(User user) {

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        return dto;
    }
    private String generateOtp() {
        int otp = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    public String verifyOtp(OtpVerificationDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found with this email");
        }

        if (!user.getOtp().equals(dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiryTime() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired. Please request a new OTP.");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiryTime(null);

        userRepository.save(user);

        return "Account verified successfully";
    }

    public Map<String, String> login(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findByEmail(loginRequestDTO.getEmail());

        if (user == null) {
            throw new RuntimeException("Email not found");
        }

        if (!user.isVerified()) {
            throw new RuntimeException("Account not verified. Please verify OTP.");
        }

        boolean passwordMatch = passwordEncoder.matches(
                loginRequestDTO.getPassword(),
                user.getPassword()
        );

        if (!passwordMatch) {
            throw new RuntimeException("Invalid password");
        }

        // 🔐 Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        Map<String, String> response = new HashMap<>();

        response.put("message", "Login successful");
        response.put("role", user.getRole());
        response.put("token", token);   // NEW

        return response;
    }

    public String resendOtp(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with this email");
        }

        if (user.isVerified()) {
            throw new RuntimeException("Account already verified");
        }

        String otp = generateOtp();

        user.setOtp(otp);
        user.setOtpExpiryTime(System.currentTimeMillis() + (5 * 60 * 1000));

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Your New OTP Code",
                "Your new OTP is: " + otp + "\nIt expires in 5 minutes."
        );

        return "New OTP sent to your email";
    }
    public String forgotPassword(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with this email");
        }

        String otp = generateOtp();

        user.setResetOtp(otp);
        user.setResetOtpExpiry(System.currentTimeMillis() + (5 * 60 * 1000));

        userRepository.save(user);

        emailService.sendEmail(
                user.getEmail(),
                "Password Reset OTP",
                "Your password reset OTP is: " + otp + "\nIt expires in 5 minutes."
        );

        return "Password reset OTP sent to your email";
    }

    public String verifyResetOtp(String email, String otp) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with this email");
        }

        if (user.getResetOtp() == null) {
            throw new OtpNotRequestedException("No reset OTP requested");
        }

        if (!user.getResetOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }

        if (user.getResetOtpExpiry() < System.currentTimeMillis()) {
            throw new OtpExpiredException("OTP expired. Please request a new OTP.");
        }

        return "OTP verified successfully";
    }
    public String resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserNotFoundException("User not found with this email");
        }

        if(user.getResetOtp() == null || user.getResetOtpExpiry() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP verification required before resetting password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        user.setResetOtp(null);
        user.setResetOtpExpiry(null);

        userRepository.save(user);

        return "Password reset successfully";
    }

}