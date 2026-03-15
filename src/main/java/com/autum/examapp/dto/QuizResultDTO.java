package com.autum.examapp.dto;

public class QuizResultDTO {

    private Integer totalQuestions;
    private Integer correctAnswers;
    private Integer wrongAnswers;
    private Double percentage;

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(Integer correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public Integer getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(Integer wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}