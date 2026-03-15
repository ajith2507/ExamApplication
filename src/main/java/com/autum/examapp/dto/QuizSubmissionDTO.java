package com.autum.examapp.dto;

import java.util.Map;

public class QuizSubmissionDTO {

    private Integer quizId;
    private String email;

    // key = questionId
    // value = selectedAnswer
    private Map<Integer, String> answers;

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }
}