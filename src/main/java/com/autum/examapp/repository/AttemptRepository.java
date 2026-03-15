package com.autum.examapp.repository;

import com.autum.examapp.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Integer> {

    List<Attempt> findByUserId(Integer userId);
    boolean existsByUserEmailAndQuizId(String userEmail, Integer quizId);

    void deleteByQuizId(Integer id);
}