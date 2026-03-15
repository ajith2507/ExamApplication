package com.autum.examapp.service;

import com.autum.examapp.dto.*;
import com.autum.examapp.exception.QuizNotFoundException;
import com.autum.examapp.model.Question;
import com.autum.examapp.model.Quiz;
import com.autum.examapp.model.User;
import com.autum.examapp.repository.QuestionRepository;
import com.autum.examapp.repository.QuizRepository;
import com.autum.examapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.autum.examapp.model.Attempt;
import com.autum.examapp.repository.AttemptRepository;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private AttemptRepository attemptRepository;

    public Question addQuestion(Integer quizId, Question question) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        question.setQuiz(quiz);

        return questionRepository.save(question);
    }



    public List<QuestionResponseDTO> getQuestionsByQuiz(Integer quizId) {

        List<Question> questions =
                questionRepository.findByQuizId(quizId);

        List<QuestionResponseDTO> responseList =
                new java.util.ArrayList<>();

        for (Question question : questions) {

            QuestionResponseDTO dto = new QuestionResponseDTO();

            dto.setId(question.getId());
            dto.setQuestionTitle(question.getQuestionTitle());
            dto.setOptionA(question.getOptionA());
            dto.setOptionB(question.getOptionB());
            dto.setOptionC(question.getOptionC());
            dto.setOptionD(question.getOptionD());

            responseList.add(dto);
        }

        return responseList;
    }
    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }





    public QuizResultDTO submitQuiz(QuizSubmissionDTO submission) {

        //  Validate quiz exists
        Quiz quiz = quizRepository.findById(submission.getQuizId())
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + submission.getQuizId()));

        // Validate answers not empty
        // Validate answers not empty
        if (submission.getAnswers() == null) {
            submission.setAnswers(new HashMap<>());
        }

        //  Fetch questions of quiz
        List<Question> questions =
                questionRepository.findByQuizId(submission.getQuizId());

        // Validate quiz has questions
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for this quiz");
        }

        int totalQuestions = questions.size();
        int correct = 0;

        for (Question question : questions) {

            Integer questionId = question.getId();

            String selectedAnswer =
                    submission.getAnswers().get(questionId);

            if (selectedAnswer != null &&
                    selectedAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                correct++;
            }
        }

        int wrong = totalQuestions - correct;

        double percentage = ((double) correct / totalQuestions) * 100;

        QuizResultDTO result = new QuizResultDTO();
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correct);
        result.setWrongAnswers(wrong);
        result.setPercentage(percentage);

        //  Save attempt


        User user = userRepository.findByEmail(submission.getEmail());




        Attempt attempt = new Attempt();
        attempt.setUser(user);
        attempt.setQuiz(quiz);
        attempt.setScore(correct);
        attempt.setTotalQuestions(totalQuestions);
        attempt.setPercentage(percentage);
        attempt.setAttemptTime(LocalDateTime.now());

        attemptRepository.save(attempt);

        return result;
    }

    public List<AttemptResponseDTO> getMyAttempts(String email) {

        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new RuntimeException("User not found: " + email);
        }

        List<Attempt> attempts = attemptRepository.findByUserId(user.getId());

        return attempts.stream().map(attempt -> {

            AttemptResponseDTO dto = new AttemptResponseDTO();

            dto.setQuizTitle(attempt.getQuiz().getTitle());
            dto.setScore(attempt.getScore());
            dto.setTotalQuestions(attempt.getTotalQuestions());
            dto.setPercentage(attempt.getPercentage());
            dto.setAttemptTime(attempt.getAttemptTime());

            return dto;

        }).collect(Collectors.toList());
    }

    public List<AdminQuestionResponseDTO> getQuestionsByQuizForAdmin(Integer quizId) {

        List<Question> questions = questionRepository.findByQuizId(quizId);

        List<AdminQuestionResponseDTO> responseList = new ArrayList<>();

        for (Question question : questions) {

            AdminQuestionResponseDTO dto = new AdminQuestionResponseDTO();

            dto.setId(question.getId());
            dto.setQuestionTitle(question.getQuestionTitle());
            dto.setOptionA(question.getOptionA());
            dto.setOptionB(question.getOptionB());
            dto.setOptionC(question.getOptionC());
            dto.setOptionD(question.getOptionD());
            dto.setCorrectAnswer(question.getCorrectAnswer());

            responseList.add(dto);
        }

        return responseList;
    }
}