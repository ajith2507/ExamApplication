package com.autum.examapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.autum.examapp.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    List<User> findByRole(String roleAdmin);
}