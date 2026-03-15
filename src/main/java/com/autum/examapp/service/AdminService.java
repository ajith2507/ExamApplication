package com.autum.examapp.service;

import com.autum.examapp.dto.UserRequestDTO;
import com.autum.examapp.model.User;
import com.autum.examapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String createAdmin(UserRequestDTO dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setVerified(true); // directly verified
        user.setRole("ROLE_ADMIN");

        userRepository.save(user);

        return "Admin created successfully";
    }

    public String deleteAdmin(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equals("ROLE_ADMIN")) {
            throw new RuntimeException("Cannot delete non-admin user");
        }

        userRepository.delete(user);

        return "Admin deleted successfully";
    }
    public List<User> getAllAdmins(){

        return userRepository.findByRole("ROLE_ADMIN");

    }
}