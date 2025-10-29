package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long examId;
    private String comments;
    private int rating; // 1-5
}