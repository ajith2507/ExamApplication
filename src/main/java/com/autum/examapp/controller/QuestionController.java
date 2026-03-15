package com.autum.examapp.controller;

import com.autum.examapp.dto.*;
import com.autum.examapp.model.Question;
import com.autum.examapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    // Only ADMIN and MAIN_ADMIN can add question
//    @PreAuthorize("hasAnyRole('ADMIN','MAIN_ADMIN')")
    @PostMapping("/add/{quizId}")
    public Question addQuestion(@PathVariable Integer quizId,
                                @RequestBody Question question) {
        return questionService.addQuestion(quizId, question);
    }

    // All authenticated users can view questions of a quiz
    @GetMapping("/quiz/{quizId}")
    public List<QuestionResponseDTO> getQuestions(@PathVariable Integer quizId) {
        return questionService.getQuestionsByQuiz(quizId);
    }

//    // Only ADMIN and MAIN_ADMIN can delete question
//    @PreAuthorize("hasAnyRole('ADMIN','MAIN_ADMIN')")
    @DeleteMapping("/deletequestion/{id}")
    public String deleteQuestion(@PathVariable Integer id) {
        questionService.deleteQuestion(id);
        return "Question deleted successfully";
    }


    @PostMapping("/submit")
    public QuizResultDTO submitQuiz(@RequestBody QuizSubmissionDTO submission) {
        return questionService.submitQuiz(submission);
    }
    @GetMapping("/attempts/my")
    public List<AttemptResponseDTO> getMyAttempts(@RequestParam String email){
        return questionService.getMyAttempts(email);
    }

    @GetMapping("/admin/quiz/{quizId}")
    public List<AdminQuestionResponseDTO> getQuestionsForAdmin(@PathVariable Integer quizId){
        return questionService.getQuestionsByQuizForAdmin(quizId);
    }
}