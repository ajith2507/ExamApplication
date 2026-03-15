package com.autum.examapp.dto;

public class QuizListResponseDTO {

    private Integer id;
    private String title;
    private String category;
    private boolean attempted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAttempted() {
        return attempted;
    }

    public void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }
}