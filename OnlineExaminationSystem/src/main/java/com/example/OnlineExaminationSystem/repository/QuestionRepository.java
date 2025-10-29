package com.example.OnlineExaminationSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.OnlineExaminationSystem.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> { 
    @Query("SELECT q FROM Question q WHERE q.exam.id = :examId")
    List<Question> findByExamId(@Param("examId") Long examId);
}
