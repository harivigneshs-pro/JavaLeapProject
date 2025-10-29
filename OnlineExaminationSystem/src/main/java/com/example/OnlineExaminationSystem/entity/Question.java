package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String questionText;
    
    @NotBlank
    private String optionA;
    
    @NotBlank
    private String optionB;
    
    @NotBlank
    private String optionC;
    
    @NotBlank
    private String optionD;
    
    @NotBlank
    private String correctAnswer;
    
    private String subject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;
}
