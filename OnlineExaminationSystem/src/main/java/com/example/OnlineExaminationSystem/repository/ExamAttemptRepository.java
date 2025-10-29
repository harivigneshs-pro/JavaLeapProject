package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.ExamAttempt;
import com.example.OnlineExaminationSystem.entity.User;
import com.example.OnlineExaminationSystem.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    Optional<ExamAttempt> findByUserAndExamAndIsTerminatedFalse(User user, Exam exam);
    Optional<ExamAttempt> findByUserAndExam(User user, Exam exam);
}