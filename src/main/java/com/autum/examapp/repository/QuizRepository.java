package com.autum.examapp.repository;

import com.autum.examapp.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findByCategory(String category);
    Page<Quiz> findByCategory(String category, Pageable pageable);
}