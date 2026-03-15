package com.autum.examapp.controller;

import com.autum.examapp.dto.UserRequestDTO;
import com.autum.examapp.model.Quiz;
import com.autum.examapp.model.User;
import com.autum.examapp.service.AdminService;
import com.autum.examapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private QuizService quizService;

    // Only MAIN_ADMIN can create new admin
//    @PreAuthorize("hasRole('MAIN_ADMIN')")
    @PostMapping("/create-admin")
    public Map<String, String> createAdmin(@RequestBody UserRequestDTO dto) {

        adminService.createAdmin(dto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin created successfully");

        return response;
    }

    // Only MAIN_ADMIN can delete admin
//    @PreAuthorize("hasAnyRole('ADMIN','MAIN_ADMIN')")
    @DeleteMapping("/delete-admin/{id}")
    public String deleteAdmin(@PathVariable Integer id) {
        return adminService.deleteAdmin(id);
    }
    @GetMapping("/all-admins")
    public List<User> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @GetMapping("/admin/all")
    public List<Quiz> getAdminQuizzes(){
        return quizService.getAllQuizzes();
    }
}