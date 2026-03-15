package com.autum.examapp.service;

import com.autum.examapp.dto.QuizListResponseDTO;
import com.autum.examapp.model.Quiz;
import com.autum.examapp.repository.AttemptRepository;
import com.autum.examapp.repository.QuestionRepository;
import com.autum.examapp.repository.QuizRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    @Transactional
    public void deleteQuiz(Integer id) {

        questionRepository.deleteByQuizId(id);
        attemptRepository.deleteByQuizId(id);

        quizRepository.deleteById(id);

    }


    public List<Quiz> getQuizzesByCategory(String category) {
        return quizRepository.findByCategory(category);
    }
    public Page<Quiz> getQuizzesWithPagination(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return quizRepository.findAll(pageable);
    }
    public Page<Quiz> getQuizzesByCategoryWithPagination(String category, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return quizRepository.findByCategory(category, pageable);
    }
    public List<QuizListResponseDTO> getAvailableQuizzes(String email){

        List<Quiz> quizzes = quizRepository.findAll();

        List<QuizListResponseDTO> response = new ArrayList<>();

        for(Quiz quiz : quizzes){

            QuizListResponseDTO dto = new QuizListResponseDTO();

            dto.setId(quiz.getId());
            dto.setTitle(quiz.getTitle());
            dto.setCategory(quiz.getCategory());

            boolean attempted =
                    attemptRepository.existsByUserEmailAndQuizId(email, quiz.getId());

            dto.setAttempted(attempted);

            response.add(dto);
        }

        return response;
    }

    public Integer fetchQuizTimeLimit(Integer id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return quiz.getTimeLimit();
    }
}