package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "exam_attempts")
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private int violationCount = 0;
    private boolean isTerminated = false;
    private boolean hasUsedGraceChance = false;
    private String violationReason;

    @CreationTimestamp
    private LocalDateTime startedAt;

    private LocalDateTime terminatedAt;
}