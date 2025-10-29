package com.example.OnlineExaminationSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> { }
