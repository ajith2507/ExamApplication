package com.autum.examapp.repository;

import com.autum.examapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    List<Question> findByQuizId(Integer quizId);

    void deleteByQuizId(Integer id);
}