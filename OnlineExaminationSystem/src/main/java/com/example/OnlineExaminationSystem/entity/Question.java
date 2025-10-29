package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String subject;

   
    // Link question to a specific exam
    @ManyToOne
    @JoinColumn(name = "exam_id")  // This creates the foreign key column in DB
    private Exam exam;// NEW: link question to a specific exam
}
