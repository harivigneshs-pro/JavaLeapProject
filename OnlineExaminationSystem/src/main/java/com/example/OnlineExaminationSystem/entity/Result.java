package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long examId;
    private int score;
    private double percentage;
}