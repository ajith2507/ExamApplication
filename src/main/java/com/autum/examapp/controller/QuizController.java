package com.autum.examapp.controller;

import com.autum.examapp.dto.QuizListResponseDTO;
import com.autum.examapp.model.Quiz;
import com.autum.examapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Only ADMIN and MAIN_ADMIN can create quiz
//    @PreAuthorize("hasAnyRole('ADMIN','MAIN_ADMIN')")
    @PostMapping("/create")
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    // All authenticated users can view quizzes
    @GetMapping("/all")
    public List<Quiz> getAllQuizzes() {

        return quizService.getAllQuizzes();
    }

    // Only ADMIN and MAIN_ADMIN can delete quiz
//    @PreAuthorize("hasAnyRole('ADMIN','MAIN_ADMIN')")
    @DeleteMapping("deletequiz/{id}")
    public String deleteQuiz(@PathVariable Integer id) {
        quizService.deleteQuiz(id);
        return "Quiz deleted successfully";
    }

    @GetMapping("/category/{category}")
    public Page<Quiz> getQuizzesByCategory(
            @PathVariable String category,
            @RequestParam int page,
            @RequestParam int size) {

        return quizService.getQuizzesByCategoryWithPagination(category, page, size);
    }
    @GetMapping
    public Page<Quiz> getAllQuizzes(
            @RequestParam int page,
            @RequestParam int size) {

        return quizService.getQuizzesWithPagination(page, size);
    }
    @GetMapping("/available-quizzes")
    public List<QuizListResponseDTO> getAvailableQuizzes(@RequestParam String email){
        return quizService.getAvailableQuizzes(email);
    }
    @GetMapping("/time/{id}")
    public Integer getQuizTimeLimit(@PathVariable Integer id) {
        return quizService.fetchQuizTimeLimit(id);
    }
}