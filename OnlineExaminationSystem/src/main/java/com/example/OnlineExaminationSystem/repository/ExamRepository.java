package com.example.OnlineExaminationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Exam;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> { }
