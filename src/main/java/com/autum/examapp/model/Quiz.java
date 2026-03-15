package com.autum.examapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer totalQuestions;
    @Column(name = "time_limit")
    private Integer timeLimit;
    @JsonManagedReference
    @OneToMany(mappedBy="quiz", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Question> questions;

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Quiz() {}

    public Quiz(String title, String category, Integer totalQuestions) {
        this.title = title;
        this.category = category;
        this.totalQuestions = totalQuestions;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
}