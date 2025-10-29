package com.example.OnlineExaminationSystem.dto;

public class ExamDTO {
    private Long id;
    private String title;

    // Constructor
    public ExamDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
